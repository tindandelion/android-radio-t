package org.dandelion.radiot.live.chat;

import java.util.List;

public interface MessageConsumer {
    void processMessages(List<Message> messages);
}
