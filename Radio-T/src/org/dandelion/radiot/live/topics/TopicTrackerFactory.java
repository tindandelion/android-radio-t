package org.dandelion.radiot.live.topics;

import de.tavendo.autobahn.WebSocketException;
import org.dandelion.radiot.live.ui.topics.TopicTracker;

public class TopicTrackerFactory implements TopicTracker.Factory {
    private final String address;

    public TopicTrackerFactory(String address) {
        this.address = address;
    }

    @Override
    public TopicTracker create() {
        try {
            return new TopicTrackerClient(trackerUrl());
        } catch (WebSocketException e) {
            throw new RuntimeException("Unable to create TopicTrackerClient", e);
        }
    }

    private String trackerUrl() {
        return String.format("ws://%s:8080/chat/current-topic", address);
    }
}
