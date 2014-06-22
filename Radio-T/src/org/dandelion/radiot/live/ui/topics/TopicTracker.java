package org.dandelion.radiot.live.ui.topics;

public interface TopicTracker {
    public interface Factory {
        TopicTracker create();
    }

    void setListener(TopicListener listener);
    void start();
}
