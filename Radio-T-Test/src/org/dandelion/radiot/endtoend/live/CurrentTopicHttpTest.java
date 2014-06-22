package org.dandelion.radiot.endtoend.live;

import org.dandelion.radiot.endtoend.live.helpers.CurrentTopicRunner;
import org.dandelion.radiot.endtoend.live.helpers.TopicTrackerServer;
import org.dandelion.radiot.live.topics.HttpTopicTrackerClient;
import org.dandelion.radiot.live.ui.topics.CurrentTopicFragment;
import org.dandelion.radiot.live.ui.topics.TopicTracker;

public class CurrentTopicHttpTest extends LiveShowActivityTestCase {
    public static final String DEFAULT_TOPIC = "What is a Web Framework?";
    private static final String TEST_SERVER_BASE_URL = "http://10.0.1.2:8080";

    private CurrentTopicRunner app;
    private TopicTrackerServer server;

    public void testShowsCurrentTopic() throws Exception {
        app.showsCurrentTopic(DEFAULT_TOPIC);
    }


    public void testWhenTopicChanges_refreshView() throws Exception {
        final String newTopic = "Amazon's ginormous public cloud turns 81 today";
        server.changeTopic(newTopic, "http://example.com");
        app.refreshTopic();
        app.showsCurrentTopic(newTopic);
    }

    @Override
    public void setUp() throws Exception {
        super.setUp();
        final HttpTopicTrackerClient trackerClient = new HttpTopicTrackerClient(TEST_SERVER_BASE_URL);

        CurrentTopicFragment.trackerFactory = new TopicTracker.Factory() {
            @Override
            public TopicTracker create() {
                return trackerClient;
            }
        };

        server = new TopicTrackerServer(TEST_SERVER_BASE_URL);
        server.changeTopic(DEFAULT_TOPIC, "http://example.com");

        app = new CurrentTopicRunner(getInstrumentation(), getActivity(), trackerClient);
    }

}
