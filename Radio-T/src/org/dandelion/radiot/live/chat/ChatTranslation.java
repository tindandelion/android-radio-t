package org.dandelion.radiot.live.chat;

public interface ChatTranslation {
    void setProgressListener(ProgressListener listener);
    void setMessageConsumer(MessageConsumer consumer);
    void start();
    void stop();

    public interface Factory {
        ChatTranslation create();
    }
}
