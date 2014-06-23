package org.dandelion.radiot.endtoend.live.helpers;

import org.dandelion.radiot.http.Consumer;
import org.dandelion.radiot.live.ui.topics.TopicTracker;

public class NullTopicTracker implements TopicTracker.Factory, TopicTracker {
    @Override
    public TopicTracker create() {
        return this;
    }

    @Override
    public void setConsumer(Consumer<String> consumer) {

    }

    @Override
    public void start() {

    }
}
