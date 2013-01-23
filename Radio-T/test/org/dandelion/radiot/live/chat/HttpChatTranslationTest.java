package org.dandelion.radiot.live.chat;

import org.junit.Test;
import org.junit.runner.RunWith;

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
    private final ErrorListener errorListener = mock(ErrorListener.class);

    @Test
    public void onStart_RequestsLastMessages() throws Exception {
        when(chatClient.retrieveMessages("last")).thenReturn(MESSAGE_LIST);
        translation.start(consumer, errorListener);

        verify(consumer).initWithMessages(MESSAGE_LIST);
    }

    @Test
    public void onRefresh_RequestsNextMessages() throws Exception {
        when(chatClient.retrieveMessages("next")).thenReturn(MESSAGE_LIST);
        translation.start(consumer, errorListener);
        translation.refresh();

        verify(consumer).appendMessages(MESSAGE_LIST);
    }

    @Test
    public void onStart_WhenErrorOccurs_CallsErrorListener() throws Exception {
        when(chatClient.retrieveMessages("last")).thenThrow(IOException.class);
        translation.start(consumer, errorListener);

        verify(errorListener).onError();
        verifyZeroInteractions(consumer);
    }

    @Test
    public void onRefresh_WhenErrorOccurs_CallsErrorListener() throws Exception {
        translation.start(consumer, errorListener);

        reset(consumer);
        when(chatClient.retrieveMessages("next")).thenThrow(IOException.class);
        translation.refresh();

        verify(errorListener).onError();
        verifyZeroInteractions(consumer);
    }
}
