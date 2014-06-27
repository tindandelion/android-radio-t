package org.dandelion.radiot.live.ui;

import org.dandelion.radiot.http.Consumer;
import org.dandelion.radiot.live.chat.Message;

import java.util.List;

public class ChatScroller implements Consumer<List<Message>> {
    private final Consumer<List<Message>> consumer;
    private final ChatStreamView view;

    public ChatScroller(Consumer<List<Message>> consumer, ChatStreamView view) {
        this.consumer = consumer;
        this.view = view;
    }

    @Override
    public void accept(List<Message> messages) {
        boolean willScroll = view.atBottom();
        consumer.accept(messages);
        if (willScroll) {
            view.scrollToBottom();
        }
    }
}
