package org.dandelion.radiot.live.ui.topics;

import org.dandelion.radiot.http.Consumer;

public interface TopicTracker {
    public interface Factory {
        TopicTracker create();
    }

    void setConsumer(Consumer<String> consumer);
    void start();
}
