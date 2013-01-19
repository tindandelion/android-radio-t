package org.dandelion.radiot.live.chat;

import org.junit.Test;
import org.junit.runner.RunWith;

import java.io.IOException;
import java.util.Collections;
import java.util.List;

import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

@SuppressWarnings("unchecked")
@RunWith(RadiotRobolectricRunner.class)
public class HttpChatTranslationTest {
    private static final List<Message> MESSAGE_LIST = Collections.emptyList();
    private final HttpChatClient chatClient = mock(HttpChatClient.class);
    private final HttpChatTranslation translation = new HttpChatTranslation(chatClient);
    private final MessageConsumer consumer = mock(MessageConsumer.class);
    private final ChatTranslation.ErrorListener errorListener = mock(ChatTranslation.ErrorListener.class);

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
    public void onError_CallsErrorListener() throws Exception {
        when(chatClient.retrieveMessages("last")).thenThrow(IOException.class);
        translation.start(consumer, errorListener);
    }
}
