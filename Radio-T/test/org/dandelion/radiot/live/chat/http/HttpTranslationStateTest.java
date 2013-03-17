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

import static org.junit.Assert.*;
import static org.mockito.Matchers.isA;
import static org.mockito.Mockito.*;

@SuppressWarnings("unchecked")
@RunWith(RadiotRobolectricRunner.class)
public class HttpTranslationStateTest {
    private static final List<Message> MESSAGES = Collections.emptyList();

    private final DeterministicScheduler scheduler  = new DeterministicScheduler();
    private final HttpChatClient chatClient = mock(HttpChatClient.class);
    private final MessageConsumer consumer = mock(MessageConsumer.class);
    private final ProgressListener listener = mock(ProgressListener.class);
    private final HttpTranslationState.StateHolder stateHolder = mock(HttpTranslationState.StateHolder.class);
    private HttpTranslationEngine engine = new HttpTranslationEngine(stateHolder, chatClient, consumer, listener, scheduler);
    private HttpTranslationState state;

    @Test
    public void disconnectedState_onStart_switchesToConnecting() throws Exception {
        state = new HttpTranslationState.Disconnected(engine);

        state.onStart();

        verify(stateHolder).changeState(isA(HttpTranslationState.Connecting.class));
    }

    @Test
    public void connectingState_whenEntered_requestsLastMessages() throws Exception {
        state = new HttpTranslationState.Connecting(engine, chatClient, consumer, listener);

        when(chatClient.retrieveMessages("last")).thenReturn(MESSAGES);
        state.enter();

        verify(consumer).processMessages(MESSAGES);
    }

    @Test
    public void connectingState_whenEntered_reportsProgress() throws Exception {
        state = new HttpTranslationState.Connecting(engine, chatClient, consumer, listener);

        when(chatClient.retrieveMessages("last")).thenReturn(MESSAGES);
        state.enter();

        verify(listener).onConnecting();
        verify(listener).onConnected();
    }

    @Test
    public void connectingState_whenEntered_reportsErrors() throws Exception {
        state = new HttpTranslationState.Connecting(engine, chatClient, consumer, listener);

        when(chatClient.retrieveMessages("last")).thenThrow(IOException.class);
        state.enter();

        verify(listener).onError();
    }

    @Test
    public void connectingState_whenConnected_switchesToListening() throws Exception {
        state = new HttpTranslationState.Connecting(engine, chatClient, consumer, listener);

        when(chatClient.retrieveMessages("last")).thenReturn(MESSAGES);
        state.enter();

        verify(stateHolder).changeState(isA(HttpTranslationState.Listening.class));
    }

    @Test
    public void connectingState_whenInterrupted_switchesToPaused() throws Exception {
        state = new HttpTranslationState.Connecting(engine, chatClient, consumer, listener);
        when(chatClient.retrieveMessages("last")).thenAnswer(new Answer<Object>() {
            @Override
            public Object answer(InvocationOnMock invocation) throws Throwable {
                state.onStop();
                return null;
            }
        });

        state.enter();

        verify(stateHolder).changeState(isA(HttpTranslationState.Paused.class));
    }

    @Test
    public void connectingState_whenError_switchesToDisconnected() throws Exception {
        state = new HttpTranslationState.Connecting(engine, chatClient, consumer, listener);

        when(chatClient.retrieveMessages("last")).thenThrow(IOException.class);
        state.enter();

        verify(stateHolder).changeState(isA(HttpTranslationState.Disconnected.class));
    }

    @Test
    public void connectingState_onStart_onlyReportsToListener() throws Exception {
        state = new HttpTranslationState.Connecting(engine, chatClient, consumer, listener);

        state.onStart();

        verify(chatClient, never()).retrieveMessages("last");
        verify(listener).onConnecting();
    }

    @Test
    public void listeningState_whenEntered_schedulesRefresh() throws Exception {
        state = new HttpTranslationState.Listening(engine, chatClient, consumer, listener, scheduler);

        when(chatClient.retrieveMessages("next")).thenReturn(MESSAGES);
        state.enter();
        scheduler.performAction();

        verify(consumer).processMessages(MESSAGES);
    }

    @Test
    public void listeningState_whenRefreshed_schedulesNextRefresh() throws Exception {
        state = new HttpTranslationState.Listening(engine, chatClient, consumer, listener, scheduler);

        when(chatClient.retrieveMessages("next")).thenReturn(MESSAGES);

        state.enter();
        scheduler.performAction();
        scheduler.performAction();

        verify(consumer, times(2)).processMessages(MESSAGES);
    }

    @Test
    public void listeningState_whenRefreshing_reportsErrors() throws Exception {
        state = new HttpTranslationState.Listening(engine, chatClient, consumer, listener, scheduler);

        when(chatClient.retrieveMessages("next")).thenThrow(IOException.class);

        state.enter();
        scheduler.performAction();

        verify(listener).onError();
    }

    @Test
    public void listeningState_onStop_cancelsScheduledRefresh() throws Exception {
        state = new HttpTranslationState.Listening(engine, chatClient, consumer, listener, scheduler);

        state.enter();
        state.onStop();

        assertFalse(scheduler.isScheduled());

    }

    @Test
    public void listeningState_onStart_requestsNextMessages() throws Exception {
        state = new HttpTranslationState.Listening(engine, chatClient, consumer, listener, scheduler);

        when(chatClient.retrieveMessages("next")).thenReturn(MESSAGES);
        state.onStart();

        verify(chatClient).retrieveMessages("next");
        verify(consumer).processMessages(MESSAGES);
        assertTrue(scheduler.isScheduled());
    }

    @Test
    public void listeningState_whenRequestAlreadyInProgress_doesNotMakeNewRequest() throws Exception {
        state = new HttpTranslationState.Listening(engine, chatClient, consumer, listener, scheduler);
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
        state = new HttpTranslationState.Listening(engine, chatClient, consumer, listener, scheduler);

        when(chatClient.retrieveMessages("next")).then(new Answer<Object>() {
            @Override
            public Object answer(InvocationOnMock invocation) throws Throwable {
                state.onStop();
                return MESSAGES;
            }
        });

        state.onStart();
        verify(stateHolder).changeState(isA(HttpTranslationState.Paused.class));
    }

    @Test
    public void pausedState_onStart_switchesToConnected() throws Exception {
        state = new HttpTranslationState.Paused(engine);

        state.onStart();
        verify(stateHolder).changeState(isA(HttpTranslationState.Listening.class));

    }
}
