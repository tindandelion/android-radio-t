package org.dandelion.radiot.endtoend.live.helpers;

import org.dandelion.radiot.live.ui.topics.TopicListener;
import org.dandelion.radiot.live.ui.topics.TopicTracker;

public class NullTopicTracker implements TopicTracker.Factory, TopicTracker {
    @Override
    public TopicTracker create() {
        return this;
    }

    @Override
    public void setListener(TopicListener listener) {

    }
}
