package org.dandelion.radiot.live.chat;

public interface ChatTranslation {
    void setProgressListener(ProgressListener listener);
    void start(MessageConsumer consumer);
    void stop();


    public interface Factory {
        ChatTranslation create();
    }
}
