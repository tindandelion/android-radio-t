package org.dandelion.radiot.live.chat;

import java.util.List;

public interface MessageConsumer {
    void appendMessages(List<Message> messages);
}
