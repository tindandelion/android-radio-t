package org.dandelion.radiot.live.chat.http;

import org.dandelion.radiot.live.chat.Message;
import org.dandelion.radiot.live.chat.MessageConsumer;
import org.dandelion.radiot.live.chat.ProgressListener;
import org.dandelion.radiot.live.schedule.Scheduler;
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

    private Scheduler scheduler  = mock(Scheduler.class);
    private final HttpChatTranslation translation = new HttpChatTranslation((HttpChatClient) null, scheduler);

    @Test
    public void disconnectedState_onStart_switchesToConnecting() throws Exception {
        HttpChatClient chatClient = mock(HttpChatClient.class);
        MessageConsumer consumer = mock(MessageConsumer.class);
        ProgressListener listener = mock(ProgressListener.class);
        HttpChatTranslation translation1 = mock(HttpChatTranslation.class);
        HttpTranslationState.Disconnected state = new HttpTranslationState.Disconnected(
                translation1, consumer, chatClient, listener);

        state.onStart();

        verify(translation1).changeState(isA(HttpTranslationState.Connecting.class));
    }

    @Test
    public void connectingState_whenEntered_requestsLastMessages() throws Exception {
        HttpChatClient chatClient = mock(HttpChatClient.class);
        MessageConsumer consumer = mock(MessageConsumer.class);
        ProgressListener listener = mock(ProgressListener.class);
        HttpTranslationState.Connecting state = new HttpTranslationState.Connecting(translation, chatClient, consumer, listener);

        when(chatClient.retrieveMessages("last")).thenReturn(MESSAGES);
        state.enter();

        verify(consumer).processMessages(MESSAGES);
    }

    @Test
    public void connectingState_whenEntered_reportsProgress() throws Exception {
        HttpChatClient chatClient = mock(HttpChatClient.class);
        MessageConsumer consumer = mock(MessageConsumer.class);
        ProgressListener listener = mock(ProgressListener.class);
        HttpTranslationState.Connecting state = new HttpTranslationState.Connecting(
                translation, chatClient, consumer, listener);

        when(chatClient.retrieveMessages("last")).thenReturn(MESSAGES);
        state.enter();

        verify(listener).onConnecting();
        verify(listener).onConnected();
    }

    @Test
    public void connectingState_whenEntered_reportsErrors() throws Exception {
        HttpChatClient chatClient = mock(HttpChatClient.class);
        MessageConsumer consumer = mock(MessageConsumer.class);
        ProgressListener listener = mock(ProgressListener.class);
        HttpTranslationState.Connecting state = new HttpTranslationState.Connecting(
                translation, chatClient, consumer, listener);

        when(chatClient.retrieveMessages("last")).thenThrow(IOException.class);
        state.enter();

        verify(listener).onError();
    }
}
