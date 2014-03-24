package org.dandelion.radiot.endtoend.live;

import com.robotium.solo.Solo;
import de.tavendo.autobahn.WebSocketConnection;
import de.tavendo.autobahn.WebSocketException;
import de.tavendo.autobahn.WebSocketHandler;
import org.dandelion.radiot.endtoend.live.helpers.TopicTrackerServer;
import org.dandelion.radiot.live.ui.topics.CurrentTopicFragment;
import org.dandelion.radiot.live.ui.topics.TopicListener;
import org.dandelion.radiot.live.ui.topics.TopicTracker;
import org.dandelion.radiot.live.ui.topics.TopicTrackerFactory;

import static org.dandelion.radiot.endtoend.live.RobotiumMatchers.showsText;
import static org.hamcrest.MatcherAssert.assertThat;


public class CurrentTopicTest extends LiveShowActivityTestCase {
    public static final String DEFAULT_TOPIC = "What is a Web Framework?";
    private static final String TEST_SERVER_ADDRESS = "10.0.1.2";

    private Solo solo;
    private TopicTrackerServer server;

    public void testShowsCurrentTopic() throws Exception {
        assertThat(solo, showsText(DEFAULT_TOPIC));
    }

    public void testWhenTopicChanges_refreshView() throws Exception {
        final String newTopic = "Amazon's ginormous public cloud turns 8 today";
        server.changeTopic(newTopic);
        assertThat(solo, showsText(newTopic));
    }

    @Override
    public void setUp() throws Exception {
        super.setUp();
        CurrentTopicFragment.trackerFactory = new TopicTrackerClient(
                String.format("ws://%s:8080/chat/current-topic", TEST_SERVER_ADDRESS));

        solo = new Solo(getInstrumentation(), getActivity());
        server = new TopicTrackerServer(TEST_SERVER_ADDRESS);
    }

    private static class TopicTrackerClient implements TopicTracker, TopicTrackerFactory {
        private TopicListener listener;
        private final WebSocketConnection connection;

        private TopicTrackerClient(String trackerUrl) throws WebSocketException {
            connection = new WebSocketConnection();
            connection.connect(trackerUrl, handler());
        }

        private WebSocketHandler handler() {
            return new WebSocketHandler() {
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

        @Override
        public TopicTracker create() {
            return this;
        }
    }
}
