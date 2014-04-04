package org.dandelion.radiot.endtoend.live;

import com.robotium.solo.Solo;
import org.dandelion.radiot.endtoend.live.helpers.TopicTrackerServer;
import org.dandelion.radiot.live.topics.TopicTrackerFactory;
import org.dandelion.radiot.live.ui.topics.CurrentTopicFragment;

import static org.dandelion.radiot.endtoend.live.RobotiumMatchers.showsText;
import static org.hamcrest.MatcherAssert.assertThat;


public class CurrentTopicTest extends LiveShowActivityTestCase {
    public static final String DEFAULT_TOPIC = "What is a Web Framework?";
    private static final String TEST_SERVER_BASE_URL = "10.0.1.2:8080/testing/chat";

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
        CurrentTopicFragment.trackerFactory = new TopicTrackerFactory(TEST_SERVER_BASE_URL);

        server = new TopicTrackerServer(TEST_SERVER_BASE_URL);
        server.changeTopic(DEFAULT_TOPIC);

        solo = new Solo(getInstrumentation(), getActivity());

    }

}
