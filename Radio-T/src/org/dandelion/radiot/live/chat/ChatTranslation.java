package org.dandelion.radiot.live.chat;

import java.util.List;

public interface ChatTranslation {
    public interface MessageConsumer {
        void addMessages(List<ChatMessage> messages);
    }
    void requestLastRecords(MessageConsumer consumer);
}
