package org.dandelion.radiot.live.chat;

public interface ChatTranslation {
    void start(MessageConsumer consumer, ErrorListener errorListener);
    void refresh();
    void stop();

    public interface ErrorListener {
        void onError();
    }

    public interface Factory {
        ChatTranslation create();
    }
}
