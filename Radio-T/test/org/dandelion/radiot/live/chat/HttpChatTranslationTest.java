package org.dandelion.radiot.live.chat;

import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.invocation.InvocationOnMock;
import org.mockito.stubbing.Answer;

import java.io.IOException;
import java.util.Collections;
import java.util.List;

import static org.mockito.Mockito.*;

@SuppressWarnings("unchecked")
@RunWith(RadiotRobolectricRunner.class)
public class HttpChatTranslationTest {
    private static final List<Message> MESSAGE_LIST = Collections.emptyList();
    private final HttpChatClient chatClient = mock(HttpChatClient.class);
    private final HttpChatTranslation translation = new HttpChatTranslation(chatClient);
    private final MessageConsumer consumer = mock(MessageConsumer.class);
    private final ProgressListener listener = mock(ProgressListener.class);

    @Test
    public void onStart_RequestsLastMessages() throws Exception {
        when(chatClient.retrieveMessages("last")).thenReturn(MESSAGE_LIST);
        translation.start(consumer, listener);

        verify(consumer).appendMessages(MESSAGE_LIST);
    }

    @Test
    public void onStart_NotifiesListener() throws Exception {
        when(chatClient.retrieveMessages("last")).thenReturn(MESSAGE_LIST);
        translation.start(consumer, listener);

        verify(listener).onConnecting();
        verify(listener).onConnected();
    }

    @Test
    public void onRefresh_RequestsNextMessages() throws Exception {
        when(chatClient.retrieveMessages("next")).thenReturn(MESSAGE_LIST);
        translation.start(consumer, listener);

        reset(consumer);
        translation.refresh();

        verify(consumer).appendMessages(MESSAGE_LIST);
    }

    @Test
    public void onStart_whenErrorOccurs_notifiesListener() throws Exception {
        when(chatClient.retrieveMessages("last")).thenThrow(IOException.class);
        translation.start(consumer, listener);

        verify(listener).onError();
        verifyZeroInteractions(consumer);
    }

    @Test
    public void onRefresh_whenErrorOccurs_NotifiesListener() throws Exception {
        translation.start(consumer, listener);

        reset(consumer);
        when(chatClient.retrieveMessages("next")).thenThrow(IOException.class);
        translation.refresh();

        verify(listener).onError();
        verifyZeroInteractions(consumer);
    }

    @Test
    public void whenStopping_ShutsDownHttpConnection() throws Exception {
        translation.stop();
        verify(chatClient).shutdown();
    }

    @Test
    public void whenStoppedWhileRunning_DoNotNotifyListenerOfErrors() throws Exception {
        when(chatClient.retrieveMessages("last")).then(new Answer<Void>() {
            @Override
            public Void answer(InvocationOnMock invocation) throws Throwable {
                translation.stop();
                throw new IOException();
            }
        });

        translation.start(consumer, listener);
        verify(listener, never()).onError();
    }
}
