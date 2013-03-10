package org.dandelion.radiot.live.chat.http;

import org.dandelion.radiot.live.chat.Message;
import org.dandelion.radiot.live.chat.MessageConsumer;
import org.dandelion.radiot.live.schedule.Scheduler;
import org.dandelion.radiot.robolectric.RadiotRobolectricRunner;
import org.junit.Test;
import org.junit.runner.RunWith;

import java.util.Collections;
import java.util.List;

import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

@RunWith(RadiotRobolectricRunner.class)
public class HttpTranslationStateTest {
    private static final List<Message> MESSAGES = Collections.emptyList();

    private Scheduler scheduler  = mock(Scheduler.class);
    private final HttpChatTranslation translation = new HttpChatTranslation((HttpChatClient) null, scheduler);

    @Test
    public void disconnectedState_onStart_requestsLastMessages() throws Exception {
        HttpChatClient chatClient = mock(HttpChatClient.class);
        MessageConsumer consumer = mock(MessageConsumer.class);
        HttpTranslationState state = new HttpTranslationState.Disconnected(translation, consumer, chatClient);

        when(chatClient.retrieveMessages("last")).thenReturn(MESSAGES);
        state.onStart();

        verify(consumer).processMessages(MESSAGES);
    }


}
