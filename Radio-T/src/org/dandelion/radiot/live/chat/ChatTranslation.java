package org.dandelion.radiot.live.chat;

public interface ChatTranslation {
    void start(MessageConsumer consumer, ProgressListener progressListener);
    void refresh();
    void stop();

    public interface Factory {
        ChatTranslation create();
    }
}
