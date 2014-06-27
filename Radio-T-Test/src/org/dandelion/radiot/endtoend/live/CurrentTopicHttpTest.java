package org.dandelion.radiot.endtoend.live;

import org.dandelion.radiot.endtoend.live.helpers.CurrentTopicRunner;
import org.dandelion.radiot.endtoend.live.helpers.TopicTrackerServer;
import org.dandelion.radiot.http.DataEngine;
import org.dandelion.radiot.http.HttpDataEngine;
import org.dandelion.radiot.live.schedule.DeterministicScheduler;
import org.dandelion.radiot.live.topics.HttpTopicProvider;
import org.dandelion.radiot.live.ui.CurrentTopicFragment;

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
        final HttpTopicProvider trackerClient = new HttpTopicProvider(TEST_SERVER_BASE_URL);
        final DeterministicScheduler scheduler = new DeterministicScheduler();

        CurrentTopicFragment.trackerFactory = new DataEngine.Factory() {
            @Override
            public DataEngine create() {
                return new HttpDataEngine(trackerClient, scheduler);
            }
        };

        server = new TopicTrackerServer(TEST_SERVER_BASE_URL);
        server.changeTopic(DEFAULT_TOPIC, "http://example.com");

        app = new CurrentTopicRunner(getInstrumentation(), getActivity(), scheduler);
    }

}
