package org.dandelion.radiot.live.ui;

import android.widget.ListView;
import org.dandelion.radiot.live.chat.Message;
import org.dandelion.radiot.live.chat.MessageConsumer;
import org.junit.Test;

import java.util.Arrays;
import java.util.List;

import static org.mockito.Mockito.*;

public class ChatScrollerTest {
    public static final List<Message> MESSAGES = Arrays.asList(new Message("", "", ""));
    private final MessageConsumer consumer = mock(MessageConsumer.class);
    private final ListView listView = mock(ListView.class);
    private final ChatScroller scroller = new ChatScroller(consumer, listView);

    @Test
    public void initWithMessages_DelegatesToConsumer() throws Exception {
        scroller.initWithMessages(MESSAGES);
        verify(consumer).initWithMessages(MESSAGES);
    }

    @Test
    public void appendMessages_DelegatesToConsumer() throws Exception {
        scroller.appendMessages(MESSAGES);
        verify(consumer).appendMessages(MESSAGES);
    }

    @Test
    public void initWithMessages_ScrollsToBottom() throws Exception {
        final int countAfter = MESSAGES.size();
        when(listView.getCount()).thenReturn(countAfter);

        scroller.initWithMessages(MESSAGES);
        verify(listView).smoothScrollToPosition(MESSAGES.size());
    }

    @Test
    public void whenAtBottomOfList_ScrollsDownAfterAppending() throws Exception {
        final int countBefore = 1;
        final int countAfter = countBefore + MESSAGES.size();
        when(listView.getLastVisiblePosition()).thenReturn(countBefore - 1);
        when(listView.getCount())
                .thenReturn(countBefore)
                .thenReturn(countAfter);

        scroller.appendMessages(MESSAGES);
        verify(listView).smoothScrollToPosition(countAfter);
    }

    @Test
    public void whenNotAtBottomOfList_KeepScrollPositionUnchanged() throws Exception {
        final int countBefore = 10;
        when(listView.getLastVisiblePosition()).thenReturn(countBefore - 3);
        when(listView.getCount()).thenReturn(countBefore);

        scroller.appendMessages(MESSAGES);
        verify(listView, never()).smoothScrollToPosition(anyInt());
    }
}
