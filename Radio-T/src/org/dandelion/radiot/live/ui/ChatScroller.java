package org.dandelion.radiot.live.ui;

import android.widget.ListView;
import org.dandelion.radiot.live.chat.Message;
import org.dandelion.radiot.live.chat.MessageConsumer;

import java.util.List;

public class ChatScroller implements MessageConsumer {
    private final MessageConsumer consumer;
    private final ListView listView;

    public ChatScroller(MessageConsumer consumer, ListView listView) {
        this.consumer = consumer;
        this.listView = listView;
    }

    @Override
    public void initWithMessages(List<Message> messages) {
        consumer.initWithMessages(messages);
        scrollToBottom();
    }

    @Override
    public void appendMessages(List<Message> messages) {
        boolean willScroll = atBottom();
        consumer.appendMessages(messages);
        if (willScroll) {
            scrollToBottom();
        }
    }

    private void scrollToBottom() {
        listView.smoothScrollToPosition(listView.getCount());
    }

    private boolean atBottom() {
        return listView.getLastVisiblePosition() == (listView.getCount() - 1);
    }
}
