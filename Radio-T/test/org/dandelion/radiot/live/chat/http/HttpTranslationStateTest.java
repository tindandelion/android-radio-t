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

import java.io.IOException;
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
        state = new HttpTranslationState.Connecting(engine, listener);

        when(chatClient.retrieveMessages("last")).thenThrow(IOException.class);
        state.enter();

        assertThat(engine.currentState(), instanceOf(HttpTranslationState.Disconnected.class));
    }

    @Test
    public void connectingState_onStart_onlyReportsToListener() throws Exception {
        state = new HttpTranslationState.Connecting(engine, listener);

        state.onStart();

        verify(chatClient, never()).retrieveMessages("last");
        verify(listener).onConnecting();
    }

    @Test
    public void listeningState_whenRefreshed_schedulesNextRefresh() throws Exception {
        state = new HttpTranslationState.Listening(engine);

        when(chatClient.retrieveMessages("next")).thenReturn(MESSAGES);

        state.enter();
        scheduler.performAction();
        scheduler.performAction();

        verify(consumer, times(2)).processMessages(MESSAGES);
    }

    @Test
    public void listeningState_whenRefreshing_reportsErrors() throws Exception {
        state = new HttpTranslationState.Listening(engine);

        when(chatClient.retrieveMessages("next")).thenThrow(IOException.class);

        state.enter();
        scheduler.performAction();

        verify(listener).onError();
    }

    @Test
    public void listeningState_onStop_cancelsScheduledRefresh() throws Exception {
        state = new HttpTranslationState.Listening(engine);

        state.enter();
        state.onStop();

        assertFalse(scheduler.isScheduled());
        assertThat(engine.currentState(), instanceOf(HttpTranslationState.Paused.class));
    }

    @Test
    public void listeningState_onStart_requestsNextMessages() throws Exception {
        state = new HttpTranslationState.Listening(engine);

        when(chatClient.retrieveMessages("next")).thenReturn(MESSAGES);
        state.onStart();

        verify(chatClient).retrieveMessages("next");
        verify(consumer).processMessages(MESSAGES);
        assertTrue(scheduler.isScheduled());
    }

    @Test
    public void listeningState_whenRequestAlreadyInProgress_doesNotMakeNewRequest() throws Exception {
        state = new HttpTranslationState.Listening(engine);
        when(chatClient.retrieveMessages("next")).then(new Answer<Object>() {
            @Override
            public Object answer(InvocationOnMock invocation) throws Throwable {
                state.onStart();
                return MESSAGES;
            }
        });

        state.onStart();

        verify(chatClient, times(1)).retrieveMessages("next");
    }

    @Test
    public void listeningState_whenInterrupted_SwitchesToPaused() throws Exception {
        state = new HttpTranslationState.Listening(engine);

        when(chatClient.retrieveMessages("next")).then(new Answer<Object>() {
            @Override
            public Object answer(InvocationOnMock invocation) throws Throwable {
                state.onStop();
                return MESSAGES;
            }
        });

        state.onStart();

        assertThat(engine.currentState(), instanceOf(HttpTranslationState.Paused.class));
    }

    @Test
    public void pausedState_onStart_switchesToConnected() throws Exception {
        state = new HttpTranslationState.Paused(engine);

        state.onStart();

        assertThat(engine.currentState(), instanceOf(HttpTranslationState.Listening.class));
    }
}
