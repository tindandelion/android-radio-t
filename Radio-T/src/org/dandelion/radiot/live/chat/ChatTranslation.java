package org.dandelion.radiot.live.chat;

public interface ChatTranslation {
    void start(MessageConsumer consumer);
    void refresh();
    void stop();

    public interface Factory {
        ChatTranslation create();
    }
}
