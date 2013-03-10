package org.dandelion.radiot.live.chat.http;

import org.dandelion.radiot.live.chat.Message;
import org.dandelion.radiot.live.chat.MessageConsumer;
import org.dandelion.radiot.live.chat.ProgressListener;
import org.dandelion.radiot.live.schedule.DeterministicScheduler;
import org.dandelion.radiot.robolectric.RadiotRobolectricRunner;
import org.junit.Test;
import org.junit.runner.RunWith;

import java.io.IOException;
import java.util.Collections;
import java.util.List;

import static org.mockito.Matchers.isA;
import static org.mockito.Mockito.*;

@SuppressWarnings("unchecked")
@RunWith(RadiotRobolectricRunner.class)
public class HttpTranslationStateTest {
    private static final List<Message> MESSAGES = Collections.emptyList();

    private final DeterministicScheduler scheduler  = new DeterministicScheduler();
    private final HttpChatClient chatClient = mock(HttpChatClient.class);
    private final HttpChatTranslation translation = new HttpChatTranslation(chatClient, scheduler);
    private final MessageConsumer consumer = mock(MessageConsumer.class);
    private final ProgressListener listener = mock(ProgressListener.class);
    private final HttpChatTranslation stateHolder = mock(HttpChatTranslation.class);
    private HttpTranslationState state;

    @Test
    public void disconnectedState_onStart_switchesToConnecting() throws Exception {
        state = new HttpTranslationState.Disconnected(stateHolder, chatClient, consumer, listener);

        state.onStart();

        verify(stateHolder).changeState(isA(HttpTranslationState.Connecting.class));
    }

    @Test
    public void connectingState_whenEntered_requestsLastMessages() throws Exception {
        state = new HttpTranslationState.Connecting(translation, chatClient, consumer, listener);

        when(chatClient.retrieveMessages("last")).thenReturn(MESSAGES);
        state.enter();

        verify(consumer).processMessages(MESSAGES);
    }

    @Test
    public void connectingState_whenEntered_reportsProgress() throws Exception {
        state = new HttpTranslationState.Connecting(translation, chatClient, consumer, listener);

        when(chatClient.retrieveMessages("last")).thenReturn(MESSAGES);
        state.enter();

        verify(listener).onConnecting();
        verify(listener).onConnected();
    }

    @Test
    public void connectingState_whenEntered_reportsErrors() throws Exception {
        state = new HttpTranslationState.Connecting(translation, chatClient, consumer, listener);

        when(chatClient.retrieveMessages("last")).thenThrow(IOException.class);
        state.enter();

        verify(listener).onError();
    }

    @Test
    public void connectingState_whenConnected_switchesToConnected() throws Exception {
        state = new HttpTranslationState.Connecting(stateHolder, chatClient, consumer, listener);

        when(chatClient.retrieveMessages("last")).thenReturn(MESSAGES);
        state.enter();

        verify(stateHolder).changeState(isA(HttpTranslationState.Connected.class));
    }

    @Test
    public void whenStarted_schedulesRefresh() throws Exception {
        translation.setMessageConsumer(consumer);

        state = new HttpTranslationState.Connected(translation, chatClient, consumer);

        when(chatClient.retrieveMessages("next")).thenReturn(MESSAGES);
        state.enter();
        scheduler.performAction();

        verify(consumer).processMessages(MESSAGES);
    }

}
