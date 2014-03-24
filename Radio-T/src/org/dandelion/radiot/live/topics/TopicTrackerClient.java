package org.dandelion.radiot.live.topics;

import de.tavendo.autobahn.WebSocketConnection;
import de.tavendo.autobahn.WebSocketException;
import de.tavendo.autobahn.WebSocketHandler;
import org.dandelion.radiot.live.ui.topics.TopicListener;
import org.dandelion.radiot.live.ui.topics.TopicTracker;

public class TopicTrackerClient implements TopicTracker {
    private TopicListener listener;
    private final WebSocketConnection connection;

    public TopicTrackerClient(String trackerUrl) throws WebSocketException {
        connection = new WebSocketConnection();
        connection.connect(trackerUrl, handler());
    }

    private WebSocketHandler handler() {
        return new WebSocketHandler() {
            @Override
            public void onOpen() {
                connection.sendTextMessage("get");
            }

            @Override
            public void onTextMessage(String payload) {
                if (listener != null) listener.onTopicChanged(payload);
            }
        };
    }

    @Override
    public void setListener(TopicListener listener) {
        this.listener = listener;
    }
}
