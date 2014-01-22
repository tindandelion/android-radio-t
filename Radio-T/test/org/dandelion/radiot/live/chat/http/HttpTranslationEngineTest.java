package org.dandelion.radiot.live.chat.http;

import org.dandelion.radiot.live.chat.Message;
import org.dandelion.radiot.live.chat.MessageConsumer;
import org.dandelion.radiot.live.chat.ProgressListener;
import org.dandelion.radiot.live.schedule.DeterministicScheduler;
import org.hamcrest.Description;
import org.hamcrest.Matcher;
import org.hamcrest.TypeSafeMatcher;
import org.json.JSONException;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.invocation.InvocationOnMock;
import org.mockito.stubbing.Answer;
import org.mockito.stubbing.OngoingStubbing;
import org.robolectric.RobolectricTestRunner;
import org.robolectric.annotation.Config;

import java.io.IOException;
import java.util.Collections;
import java.util.List;

import static org.hamcrest.MatcherAssert.assertThat;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertTrue;
import static org.mockito.Mockito.*;

@SuppressWarnings("unchecked")
@RunWith(RobolectricTestRunner.class)
@Config(manifest = Config.NONE)
public class HttpTranslationEngineTest {
    private final DeterministicScheduler scheduler  = new DeterministicScheduler();
    private final HttpChatClient chatClient = mock(HttpChatClient.class);
    private final MessageConsumer consumer = mock(MessageConsumer.class);
    private final ProgressListener listener = mock(ProgressListener.class);
    private final HttpTranslationEngine engine =
            new HttpTranslationEngine(chatClient, scheduler);

    @Test
    public void shutdown_terminatesChatClient_andGoesToDisconnected() throws Exception {
        engine.startListening();

        engine.shutdown();

        verify(chatClient).shutdown();
        assertThat(engine, isInState("Disconnected"));
    }

    @Test
    public void whenDisconnected_onStop_doesNothing() throws Exception {
        engine.disconnect();
        engine.start();
    }

    @Test
    public void whenDisconnected_onStart_startsConnecting() throws Exception {
        engine.disconnect();
        whileRetrievingMessagesDo(new Runnable() {
            @Override
            public void run() {
                assertThat(engine, isInState("Connecting"));
            }
        });

        engine.start();

        verify(chatClient).retrieveMessages();
    }

    @Test
    public void whenDisconnected_andPreviousNetworkRequestCompletes_doesNothing() throws Exception {
        engine.disconnect();
        engine.processMessages(messageList());
    }

    @Test
    public void whenDisconnected_andPreviousNetworkRequestFails_doesNothing() throws Exception {
        engine.disconnect();
        engine.onError();
    }

    @Test
    public void whenDisconnected_andPollScheduleEventOccures_doesNothing() throws Exception {
        engine.disconnect();
        scheduler.performAction();
        assertFalse(scheduler.isScheduled());
    }

    @Test
    public void whenConnecting_onStart_switchesToConnectingStateWhileRetrievingMessages() throws Exception {
        whileRetrievingMessagesDo(new Runnable() {
            @Override
            public void run() {
                assertThat(engine, isInState("Connecting"));
            }
        });

        engine.startConnecting();
    }

    @Test
    public void whenConnecting_onStop_switchesToPausedConnecting() throws Exception {
        whileRetrievingMessagesDo(new Runnable() {
            @Override
            public void run() {
                engine.stop();
                assertThat(engine, isInState("PausedConnecting"));
            }
        });
        engine.start();
    }

    @Test
    public void whenConnecting_andPreviousNetworkRequestCompletes_feedsMessagesToConsumerAndGoesToListening() throws Exception {
        final List<Message> messages = messageList();

        when(chatClient.retrieveMessages()).thenReturn(messages);
        engine.startConnecting();

        verify(consumer).processMessages(messages);
        assertThat(engine, isInState("Listening"));
    }

    @Test
    public void whenConnecting_andPreviousNetworkRequestFails_NotifiesListenerAndGoesToDisconnected() throws Exception {
        whenRetrievingMessages().thenThrow(IOException.class);

        engine.startConnecting();

        verify(listener).onError();
        assertThat(engine, isInState("Disconnected"));
    }

    @Test
    public void whenConnecting_andPollScheduleEventOccures_doesNothing() throws Exception {
        whileRetrievingMessagesDo(new Runnable() {
            @Override
            public void run() {
                scheduler.performAction();
                assertFalse(scheduler.isScheduled());
            }
        });

        engine.startConnecting();
    }


    @Test
    public void whenConnecting_notifiesListenerOfProgress() throws Exception {
        whileRetrievingMessagesDo(new Runnable() {
            @Override
            public void run() {
                verify(listener).onConnecting();
            }
        });

        engine.start();

        verify(listener).onConnected();
    }

    @Test
    public void whenPausedConnecting_onStart_notifiesListenerAndGoesToConnecting() throws Exception {
        engine.pauseConnecting();

        engine.start();

        verify(listener).onConnecting();
        assertThat(engine, isInState("Connecting"));
    }

    @Test
    public void whenPausedConnecting_onStop_doesNothing() throws Exception {
        engine.pauseConnecting();
        engine.stop();
    }

    @Test
    public void whenPausedConnecting_andPreviousNetworkRequestCompletes_notifiesListenerAndGoesToPausedListening() throws Exception {
        engine.pauseConnecting();
        engine.processMessages(messageList());

        verify(listener).onConnected();
        assertThat(engine, isInState("PausedListening"));
    }

    @Test
    public void whenPausedConnecting_andPreviousNetworkRequestFails_notifiesListenerAndGoesToDisconnected() throws Exception {
        engine.pauseConnecting();
        engine.onError();

        verify(listener).onError();
        assertThat(engine, isInState("Disconnected"));
    }

    @Test
    public void whenPausedConnecting_andPollScheduleEventOccures_doesNothing() throws Exception {
        engine.pauseConnecting();
        scheduler.performAction();
        assertFalse(scheduler.isScheduled());
    }

    @Test
    public void whenStartsListening_schedulesPolling() throws Exception {
        engine.startListening();

        assertThat(engine, isInState("Listening"));
        assertTrue(scheduler.isScheduled());
    }

    @Test
    public void whenListening_onStart_doesNothing() throws Exception {
        engine.startListening();
        engine.start();
    }


    @Test
    public void whenListening_onStop_cancelsPollingAndGoesToPausedListening() throws Exception {
        engine.startListening();
        engine.stop();

        assertFalse(scheduler.isScheduled());
        assertThat(engine, isInState("PausedListening"));
    }

    @Test
    public void whenListening_pollsForNextMessages_onSchedule() throws Exception {
        engine.startListening();

        List<Message> nextMessages = messageList();
        whenRetrievingMessages().thenReturn(nextMessages);

        scheduler.performAction();

        verify(chatClient).retrieveMessages();
        verify(consumer).processMessages(nextMessages);
    }

    @Test
    public void whenListening_afterPolling_schedulesNextPoll() throws Exception {
        engine.startListening();

        scheduler.performAction();

        assertTrue(scheduler.isScheduled());
    }

    @Test
    public void whenListening_andNetworkRequestFails_notifiesListenerAndGoesDisconnected() throws Exception {
        engine.startListening();
        whenRetrievingMessages().thenThrow(IOException.class);

        scheduler.performAction();

        verify(listener).onError();
        assertThat(engine, isInState("Disconnected"));
    }

    @Test
    public void whenListening_andNetworkRequestCompletes_schedulesNextPoll() throws Exception {
        engine.startListening();
        engine.processMessages(messageList());
    }

    @Test
    public void whenPausedListening_onStart_schedulesNextPoll() throws Exception {
        engine.pauseListening();

        engine.start();

        assertTrue(scheduler.isScheduled());
        assertThat(engine, isInState("Listening"));
    }

    @Test
    public void whenPausedListening_onStop_doesNothing() throws Exception {
        engine.pauseListening();

        engine.stop();
    }

    @Test
    public void whenPausedListening_andNetworkRequestCompletes_doesNotScheduleNextPoll() throws Exception {
        engine.pauseListening();
        engine.processMessages(Collections.<Message>emptyList());

        assertFalse(scheduler.isScheduled());
        assertThat(engine, isInState("PausedListening"));
    }

    @Test
    public void whenPausedListening_andNetworkRequestFails_goesDisconnected() throws Exception {
        engine.pauseListening();
        engine.onError();

        assertThat(engine, isInState("Disconnected"));
    }

    @Test
    public void whenPausedListening_andPollScheduleEventOccurs_doesNothing() throws Exception {
        engine.pauseListening();
        engine.performAction();
        assertFalse(scheduler.isScheduled());
    }

    @Test
    public void whenShutdownWhileStarting_SuppressAllFurtherNotifications() throws Exception {
        whileRetrievingMessagesDo(new Runnable() {
            @Override
            public void run() {
                engine.shutdown();
            }
        });

        engine.startConnecting();

        verify(listener, never()).onConnected();
        verify(listener, never()).onError();
        verify(consumer, never()).processMessages(anyList());
    }

    @Test
    public void whenShutdownWhileRefreshing_SuppressMessageConsuming() throws Exception {
        whileRetrievingMessagesDo(new Runnable() {
            @Override
            public void run() {
                engine.shutdown();
            }
        });

        engine.startListening();
        scheduler.performAction();

        verify(consumer, never()).processMessages(anyList());
    }

    @Before
    public void setUp() throws Exception {
        engine.setProgressListener(listener);
        engine.setMessageConsumer(consumer);
    }

    private Matcher<? super HttpTranslationEngine> isInState(final String state) {
        return new TypeSafeMatcher<HttpTranslationEngine>() {
            @Override
            protected boolean matchesSafely(HttpTranslationEngine engine) {
                return state.equals(engine.currentState());
            }

            @Override
            public void describeTo(Description description) {
                description.appendText("Engine in state").appendValue(state);
            }

            @Override
            protected void describeMismatchSafely(HttpTranslationEngine engine, Description mismatchDescription) {
                mismatchDescription.appendText("Engine in state").appendValue(engine.currentState());
            }
        };
    }

    private List<Message> messageList() {
        return Collections.emptyList();
    }

    private OngoingStubbing<List<Message>> whenRetrievingMessages() throws IOException, JSONException {
        return when(chatClient.retrieveMessages());
    }

    private void whileRetrievingMessagesDo(final Runnable action) throws IOException, JSONException {
        whenRetrievingMessages().thenAnswer(new Answer<Object>() {
            @Override
            public Object answer(InvocationOnMock invocation) throws Throwable {
                action.run();
                return messageList();
            }
        });
    }
}
