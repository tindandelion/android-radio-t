package org.dandelion.radiot.live.ui;

import org.dandelion.radiot.live.chat.Message;
import org.dandelion.radiot.live.chat.MessageConsumer;

import java.util.List;

public class ChatScroller implements MessageConsumer {
    private final MessageConsumer consumer;
    private final ChatStreamView view;

    public ChatScroller(MessageConsumer consumer, ChatStreamView view) {
        this.consumer = consumer;
        this.view = view;
    }

    @Override
    public void appendMessages(List<Message> messages) {
        boolean willScroll = view.atBottom();
        consumer.appendMessages(messages);
        if (willScroll) {
            view.scrollToBottom();
        }
    }
}
