package org.dandelion.radiot.live.chat.http;

import org.dandelion.radiot.live.chat.Message;
import org.dandelion.radiot.live.chat.MessageConsumer;
import org.dandelion.radiot.live.chat.ProgressListener;
import org.dandelion.radiot.live.schedule.DeterministicScheduler;
import org.dandelion.radiot.robolectric.RadiotRobolectricRunner;
import org.hamcrest.Description;
import org.hamcrest.Matcher;
import org.hamcrest.TypeSafeMatcher;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.invocation.InvocationOnMock;
import org.mockito.stubbing.Answer;

import java.io.IOException;
import java.util.Collections;
import java.util.List;

import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.Matchers.instanceOf;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertTrue;
import static org.mockito.Mockito.*;

@SuppressWarnings("unchecked")
@RunWith(RadiotRobolectricRunner.class)
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
        assertThat(engine, isInState(HttpTranslationState.Disconnected.class));
    }

    @Test
    public void whenDisconnected_onStop_doesNothing() throws Exception {
        engine.disconnect();
        engine.currentState().onStop();
    }

    @Test
    public void whenDisconnected_onStart_startsConnecting() throws Exception {
        engine.disconnect();
        when(chatClient.retrieveMessages("last")).thenAnswer(new Answer<Object>() {
            @Override
            public Object answer(InvocationOnMock invocation) throws Throwable {
                assertThat(engine, isInState(HttpTranslationState.Connecting.class));
                return messageList();
            }
        });

        engine.currentState().onStart();

        verify(chatClient).retrieveMessages("last");
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
        when(chatClient.retrieveMessages("last")).thenAnswer(new Answer<Object>() {
            @Override
            public Object answer(InvocationOnMock invocation) throws Throwable {
                assertThat(engine, isInState(HttpTranslationState.Connecting.class));
                return messageList();
            }
        });

        engine.startConnecting();
    }

    @Test
    public void whenConnecting_onStop_switchesToPausedConnecting() throws Exception {
        when(chatClient.retrieveMessages("last")).thenAnswer(new Answer<Object>() {
            @Override
            public Object answer(InvocationOnMock invocation) throws Throwable {
                engine.currentState().onStop();
                assertThat(engine, isInState(HttpTranslationState.PausedConnecting.class));
                return messageList();
            }
        });
        engine.currentState().onStart();
    }

    @Test
    public void whenConnecting_andPreviousNetworkRequestCompletes_feedsMessagesToConsumerAndGoesToListening() throws Exception {
        final List<Message> messages = messageList();

        when(chatClient.retrieveMessages("last")).thenReturn(messages);
        engine.startConnecting();

        verify(consumer).processMessages(messages);
        assertThat(engine, isInState(HttpTranslationState.Listening.class));
    }

    @Test
    public void whenConnecting_andPreviousNetworkRequestFails_NotifiesListenerAndGoesToDisconnected() throws Exception {
        when(chatClient.retrieveMessages("last")).thenThrow(IOException.class);

        engine.startConnecting();

        verify(listener).onError();
        assertThat(engine, isInState(HttpTranslationState.Disconnected.class));
    }

    @Test
    public void whenConnecting_andPollScheduleEventOccures_doesNothing() throws Exception {
        when(chatClient.retrieveMessages("last")).thenAnswer(new Answer<Object>() {
            @Override
            public Object answer(InvocationOnMock invocation) throws Throwable {
                scheduler.performAction();
                assertFalse(scheduler.isScheduled());
                return messageList();
            }
        });

        engine.startConnecting();
    }

    @Test
    public void whenConnecting_notifiesListenerOfProgress() throws Exception {
        when(chatClient.retrieveMessages("last")).thenAnswer(new Answer<Object>() {
            @Override
            public Object answer(InvocationOnMock invocation) throws Throwable {
                verify(listener).onConnecting();
                return null;
            }
        });
        engine.currentState().onStart();

        verify(listener).onConnected();
    }

    @Test
    public void whenPausedConnecting_onStart_notifiesListenerAndGoesToConnecting() throws Exception {
        engine.pauseConnecting();

        engine.currentState().onStart();

        verify(listener).onConnecting();
        assertThat(engine, isInState(HttpTranslationState.Connecting.class));
    }

    @Test
    public void whenPausedConnecting_onStop_doesNothing() throws Exception {
        engine.pauseConnecting();
        engine.currentState().onStop();
    }

    @Test
    public void whenPausedConnecting_andPreviousNetworkRequestCompletes_notifiesListenerAndGoesToPausedListening() throws Exception {
        engine.pauseConnecting();
        engine.processMessages(Collections.<Message>emptyList());

        verify(listener).onConnected();
        assertThat(engine, isInState(HttpTranslationState.PausedListening.class));
    }

    @Test
    public void whenPausedConnecting_andPreviousNetworkRequestFails_notifiesListenerAndGoesToDisconnected() throws Exception {
        engine.pauseConnecting();
        engine.onError();

        verify(listener).onError();
        assertThat(engine, isInState(HttpTranslationState.Disconnected.class));
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

        assertThat(engine, isInState(HttpTranslationState.Listening.class));
        assertTrue(scheduler.isScheduled());
    }

    @Test
    public void whenListening_onStart_doesNothing() throws Exception {
        engine.startListening();
        engine.currentState().onStart();
    }


    @Test
    public void whenListening_onStop_cancelsPollingAndGoesToPausedListening() throws Exception {
        engine.startListening();
        engine.currentState().onStop();

        assertFalse(scheduler.isScheduled());
        assertThat(engine, isInState(HttpTranslationState.PausedListening.class));
    }

    @Test
    public void whenListening_pollsForNextMessages_onSchedule() throws Exception {
        engine.startListening();

        List<Message> nextMessages = messageList();
        when(chatClient.retrieveMessages("next")).thenReturn(nextMessages);

        scheduler.performAction();

        verify(chatClient).retrieveMessages("next");
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
        when(chatClient.retrieveMessages("next")).thenThrow(IOException.class);

        scheduler.performAction();

        verify(listener).onError();
        assertThat(engine, isInState(HttpTranslationState.Disconnected.class));
    }

    @Test
    public void whenPausedListening_onStart_schedulesNextPoll() throws Exception {
        engine.pauseListening();

        engine.currentState().onStart();

        assertTrue(scheduler.isScheduled());
        assertThat(engine, isInState(HttpTranslationState.Listening.class));
    }

    @Test
    public void whenPausedListening_onStop_doesNothing() throws Exception {
        engine.pauseListening();

        engine.currentState().onStop();
    }

    @Test
    public void whenPausedListening_andNetworkRequestCompletes_doesNotScheduleNextPoll() throws Exception {
        engine.pauseListening();
        engine.processMessages(Collections.<Message>emptyList());

        assertFalse(scheduler.isScheduled());
        assertThat(engine, isInState(HttpTranslationState.PausedListening.class));
    }

    @Test
    public void whenPausedListening_andNetworkRequestFails_goesDisconnected() throws Exception {
        engine.pauseListening();
        engine.onError();

        assertThat(engine, isInState(HttpTranslationState.Disconnected.class));
    }

    @Test
    public void whenPausedListening_andPollScheduleEventOccures_doesNothing() throws Exception {
        engine.pauseListening();
        engine.performAction();
        assertFalse(scheduler.isScheduled());
    }

    @Before
    public void setUp() throws Exception {
        engine.setProgressListener(listener);
        engine.setMessageConsumer(consumer);
    }

    private Matcher<? super HttpTranslationEngine> isInState(final Class<? extends HttpTranslationState> aClass) {
        return new TypeSafeMatcher<HttpTranslationEngine>() {
            @Override
            protected boolean matchesSafely(HttpTranslationEngine httpTranslationEngine) {
                return instanceOf(aClass).matches(httpTranslationEngine.currentState());
            }

            @Override
            public void describeTo(Description description) {
                description.appendText("Engine in state").appendValue(aClass);
            }

            @Override
            protected void describeMismatchSafely(HttpTranslationEngine item, Description mismatchDescription) {
                mismatchDescription.appendText("Engine in state").appendValue(item.currentState().getClass());
            }
        };
    }

    private List<Message> messageList() {
        return Collections.emptyList();
    }

}
