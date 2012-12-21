package org.dandelion.radiot.live.chat;

import java.util.List;

public interface ChatTranslation {
    public interface MessageConsumer {
        void initWithMessages(List<ChatMessage> messages);
        void appendMessages(List<ChatMessage> messages);
    }
    void requestLastRecords(MessageConsumer consumer);
    void requestNextRecords(MessageConsumer consumer);
}
