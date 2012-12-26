package org.dandelion.radiot.live.chat;

import java.util.List;

public interface MessageConsumer {
    void initWithMessages(List<Message> messages);
    void appendMessages(List<Message> messages);
}
