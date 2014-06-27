package org.dandelion.radiot.live.ui;

import org.dandelion.radiot.http.Consumer;
import org.dandelion.radiot.live.chat.Message;
import org.junit.Test;

import java.util.Arrays;
import java.util.List;

import static org.mockito.Mockito.*;

public class ChatScrollerTest {
    public static final List<Message> MESSAGES = Arrays.asList(new Message("", "", "", 0));
    private final Consumer consumer = mock(Consumer.class);
    private final ChatStreamView chatView = mock(ChatStreamView.class);
    private final ChatScroller scroller = new ChatScroller(consumer, chatView);

    @Test
    public void appendMessages_DelegatesToConsumer() throws Exception {
        scroller.accept(MESSAGES);
        verify(consumer).accept(MESSAGES);
    }

    @Test
    public void whenAtBottomOfList_ScrollsDownAfterAppending() throws Exception {
        when(chatView.atBottom()).thenReturn(true);

        scroller.accept(MESSAGES);
        verify(chatView).scrollToBottom();
    }

    @Test
    public void whenNotAtBottomOfList_KeepScrollPositionUnchanged() throws Exception {
        when(chatView.atBottom()).thenReturn(false);
        scroller.accept(MESSAGES);
        verify(chatView, never()).scrollToBottom();
    }
}
