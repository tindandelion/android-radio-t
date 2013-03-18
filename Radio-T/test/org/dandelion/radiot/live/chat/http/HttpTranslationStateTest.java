package org.dandelion.radiot.live.chat.http;

import org.dandelion.radiot.live.chat.Message;
import org.dandelion.radiot.live.chat.MessageConsumer;
import org.dandelion.radiot.live.chat.ProgressListener;
import org.dandelion.radiot.live.schedule.DeterministicScheduler;
import org.dandelion.radiot.robolectric.RadiotRobolectricRunner;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.invocation.InvocationOnMock;
import org.mockito.stubbing.Answer;

import java.util.Collections;
import java.util.List;

import static org.hamcrest.Matchers.instanceOf;
import static org.junit.Assert.*;
import static org.mockito.Mockito.*;

@SuppressWarnings("unchecked")
@RunWith(RadiotRobolectricRunner.class)
public class HttpTranslationStateTest {
    private static final List<Message> MESSAGES = Collections.emptyList();

    private final DeterministicScheduler scheduler  = new DeterministicScheduler();
    private final HttpChatClient chatClient = mock(HttpChatClient.class);
    private final MessageConsumer consumer = mock(MessageConsumer.class);
    private final ProgressListener listener = mock(ProgressListener.class);
    private HttpTranslationEngine engine = new HttpTranslationEngine(chatClient, consumer, listener, scheduler);
    private HttpTranslationEngine mockEngine = mock(HttpTranslationEngine.class);

    private HttpTranslationState state;

    @Test
    public void disconnectedState_onStart_switchesToConnecting() throws Exception {
        state = new HttpTranslationState.Disconnected(mockEngine);

        state.onStart();

        verify(mockEngine).connectToChat();
    }

    @Test
    public void connectingState_whenError_switchesToDisconnected() throws Exception {
        state = new HttpTranslationState.Connecting(mockEngine, listener);

        state.onError();

        verify(mockEngine).disconnect();
    }

    @Test
    public void connectingState_onStart_onlyReportsToListener() throws Exception {
        state = new HttpTranslationState.Connecting(mockEngine, listener);

        state.onStart();

        verify(mockEngine, never()).connectToChat();
        verify(listener).onConnecting();
    }

    @Test
    public void connectingState_onRequestCompleted_notifiesListener() throws Exception {
        state = new HttpTranslationState.Connecting(mockEngine, listener);

        state.onRequestCompleted();

        verify(listener).onConnected();
    }

    @Test
    public void listeningState_onStop_cancelsScheduledRefresh() throws Exception {
        state = new HttpTranslationState.Listening(engine);

        state.onStop();

        assertFalse(scheduler.isScheduled());
        assertThat(engine.currentState(), instanceOf(HttpTranslationState.Paused.class));
    }

    @Test
    public void listeningState_onStart_schedulesPolling() throws Exception {
        state = new HttpTranslationState.Listening(engine);

        when(chatClient.retrieveMessages("next")).thenReturn(MESSAGES);

        state.onStart();
        assertTrue(scheduler.isScheduled());

        scheduler.performAction();

        verify(chatClient).retrieveMessages("next");
        verify(consumer).processMessages(MESSAGES);
        assertTrue(scheduler.isScheduled());
    }

    @Test
    public void listeningState_whenInterrupted_SwitchesToPaused() throws Exception {
        state = new HttpTranslationState.Listening(engine);

        when(chatClient.retrieveMessages("next")).then(new Answer<Object>() {
            @Override
            public Object answer(InvocationOnMock invocation) throws Throwable {
                return MESSAGES;
            }
        });

        engine.currentState = state;
        state.onStart();
        state.onStop();

        assertThat(engine.currentState(), instanceOf(HttpTranslationState.Paused.class));
    }

    @Test
    public void pausedState_onStart_switchesToListening() throws Exception {
        state = new HttpTranslationState.Paused(engine);

        state.onStart();

        assertThat(engine.currentState(), instanceOf(HttpTranslationState.Listening.class));
    }
}
