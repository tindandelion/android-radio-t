package org.dandelion.radiot.live.topics;

import org.dandelion.radiot.live.ui.topics.TopicListener;
import org.dandelion.radiot.live.ui.topics.TopicTracker;

public class TopicTrackerFactory implements TopicTracker.Factory {
    private final String baseUrl;

    public TopicTrackerFactory(String baseUrl) {
        this.baseUrl = baseUrl;
    }

    @Override
    public TopicTracker create() {
        return nullTopicTracker();
    }

    private TopicTracker nullTopicTracker() {
        return new TopicTracker() {

            @Override
            public void setListener(TopicListener listener) {

            }

            @Override
            public void start() {

            }
        };
    }

}
