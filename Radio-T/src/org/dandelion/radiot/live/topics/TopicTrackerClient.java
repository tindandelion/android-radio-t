package org.dandelion.radiot.live.topics;

import de.tavendo.autobahn.WebSocketConnection;
import de.tavendo.autobahn.WebSocketException;
import de.tavendo.autobahn.WebSocketHandler;
import org.dandelion.radiot.live.ui.topics.TopicListener;
import org.dandelion.radiot.live.ui.topics.TopicTracker;
import org.json.JSONException;
import org.json.JSONObject;

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
                if (notEmpty(payload) && listener != null)
                    listener.onTopicChanged(extractTopic(payload));
            }

            private String extractTopic(String json) {
                try {
                    JSONObject obj = new JSONObject(json);
                    return obj.getString("text");
                } catch (JSONException e) {
                    throw new RuntimeException(e);
                }

            }

            private boolean notEmpty(String payload) {
                return payload.trim().length() > 0;
            }
        };
    }

    @Override
    public void setListener(TopicListener listener) {
        this.listener = listener;
    }
}
