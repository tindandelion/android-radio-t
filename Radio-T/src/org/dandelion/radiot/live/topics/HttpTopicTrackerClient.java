package org.dandelion.radiot.live.topics;

import org.dandelion.radiot.live.ui.topics.TopicListener;
import org.dandelion.radiot.live.ui.topics.TopicTracker;

public class HttpTopicTrackerClient implements TopicTracker {
    private TopicListener listener;
    private final String defaultTopic;

    public HttpTopicTrackerClient(String defaultTopic) {
        this.defaultTopic = defaultTopic;
    }

    @Override
    public void setListener(TopicListener listener) {
        this.listener = listener;
    }

    @Override
    public void start() {
        listener.onTopicChanged(defaultTopic);
    }

    public void refreshTopic(String newTopic) {
        listener.onTopicChanged(newTopic);
    }
}
