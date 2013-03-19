package org.dandelion.radiot.live.chat.http;

import org.dandelion.radiot.live.chat.Message;
import org.dandelion.radiot.live.chat.MessageConsumer;
import org.dandelion.radiot.live.chat.ProgressListener;
import org.dandelion.radiot.live.schedule.DeterministicScheduler;
import org.dandelion.radiot.robolectric.RadiotRobolectricRunner;
import org.hamcrest.Description;
import org.hamcrest.Matcher;
import org.hamcrest.TypeSafeMatcher;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.invocation.InvocationOnMock;
import org.mockito.stubbing.Answer;

import java.io.IOException;
import java.util.ArrayList;
import java.util.Collections;

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
            new HttpTranslationEngine(chatClient, consumer, listener, scheduler);

    @Test
    public void disconnect() throws Exception {
        engine.disconnect();
        assertThat(engine, isInState(HttpTranslationState.Disconnected.class));
    }

    @Test
    public void whenConnecting_switchesToConnectingState_whileRetrievingMessages() throws Exception {
        final ArrayList<Message> messages = new ArrayList<Message>();

        when(chatClient.retrieveMessages("last")).thenAnswer(new Answer<Object>() {
            @Override
            public Object answer(InvocationOnMock invocation) throws Throwable {
                assertThat(engine, isInState(HttpTranslationState.Connecting.class));
                return messages;
            }
        });
        engine.connect();

        verify(consumer).processMessages(messages);
        assertThat(engine, isInState(HttpTranslationState.Listening.class));
    }

    @Test
    public void whenConnecting_reportsProgress() throws Exception {
        when(chatClient.retrieveMessages("last")).thenAnswer(new Answer<Object>() {
            @Override
            public Object answer(InvocationOnMock invocation) throws Throwable {
                verify(listener).onConnecting();
                return null;
            }
        });
        engine.connect();

        verify(listener).onConnected();
    }

    @Test
    public void whenConnecting_reportsError_andGoesDisconnected() throws Exception {
        when(chatClient.retrieveMessages("last")).thenThrow(IOException.class);

        engine.connect();

        verify(listener).onError();
        assertThat(engine, isInState(HttpTranslationState.Disconnected.class));
    }

    @Test
    public void whenConnecting_butInterrupted_switchesToPaused() throws Exception {
        when(chatClient.retrieveMessages("last")).thenAnswer(new Answer<Object>() {
            @Override
            public Object answer(InvocationOnMock invocation) throws Throwable {
                engine.currentState().onStop();
                return null;
            }
        });
        engine.connect();

        assertThat(engine, isInState(HttpTranslationState.Paused.class));
    }

    @Test
    public void whenRestarted_whileConnecting_restoresConnectingState() throws Exception {
        when(chatClient.retrieveMessages("last")).thenAnswer(new Answer<Object>() {
            @Override
            public Object answer(InvocationOnMock invocation) throws Throwable {
                engine.currentState().onStop();
                engine.currentState().onStart();
                return null;
            }
        });
        engine.connect();

        assertThat(engine, isInState(HttpTranslationState.Connecting.class));
    }

    @Test
    public void whenListening_schedulesPolling() throws Exception {
        engine.startListening();

        assertThat(engine, isInState(HttpTranslationState.Listening.class));
        assertTrue(scheduler.isScheduled());
    }

    @Test
    public void whenListening_pollsForNextMessages() throws Exception {
        ArrayList<Message> nextMessages = new ArrayList<Message>();
        when(chatClient.retrieveMessages("next")).thenReturn(nextMessages);

        engine.startListening();
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
    public void whenListening_reportsError_andGoesDisconnected() throws Exception {
        engine.startListening();
        when(chatClient.retrieveMessages("next")).thenThrow(IOException.class);

        scheduler.performAction();

        verify(listener).onError();
        assertThat(engine, isInState(HttpTranslationState.Disconnected.class));
    }

    @Test
    public void whenStopsListening_cancelsPolling_andGoesToPaused() throws Exception {
        engine.startListening();
        engine.stopListening();

        assertFalse(scheduler.isScheduled());
        assertThat(engine, isInState(HttpTranslationState.Paused.class));
    }

    @Test
    public void whenPaused_andNetworkRequestCompletes_doesNotScheduleNextPoll() throws Exception {
        engine.stopListening();
        engine.processMessages(Collections.<Message>emptyList());

        assertFalse(scheduler.isScheduled());
        assertThat(engine, isInState(HttpTranslationState.Paused.class));
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
}
