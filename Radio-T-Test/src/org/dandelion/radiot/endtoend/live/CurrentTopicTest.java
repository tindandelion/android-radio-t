package org.dandelion.radiot.endtoend.live;

import android.widget.TextView;
import com.robotium.solo.Solo;
import org.dandelion.radiot.endtoend.live.helpers.TopicTrackerServer;
import org.dandelion.radiot.helpers.async.Probe;
import org.dandelion.radiot.live.topics.TopicTrackerFactory;
import org.dandelion.radiot.live.ui.topics.CurrentTopicFragment;
import org.hamcrest.Description;

import static org.dandelion.radiot.helpers.async.Poller.assertEventually;


public class CurrentTopicTest extends LiveShowActivityTestCase {
    public static final String DEFAULT_TOPIC = "What is a Web Framework?";
    private static final String TEST_SERVER_BASE_URL = "10.0.1.2:8080/testing/chat";

    private Solo solo;
    private TopicTrackerServer server;

    public void testShowsCurrentTopic() throws Exception {
        assertCurrentTopic(DEFAULT_TOPIC);
    }


    public void testWhenTopicChanges_refreshView() throws Exception {
        final String newTopic = "Amazon's ginormous public cloud turns 8 today";
        server.changeTopic(newTopic, "http://example.com");
        assertCurrentTopic(newTopic);
    }

    public void testDoesNotReactToHeartbeatMessages() throws Exception {
        server.heartbeat();
        assertCurrentTopic(DEFAULT_TOPIC);
    }

    @Override
    public void setUp() throws Exception {
        super.setUp();
        CurrentTopicFragment.trackerFactory = new TopicTrackerFactory(TEST_SERVER_BASE_URL);

        server = new TopicTrackerServer(TEST_SERVER_BASE_URL);
        server.changeTopic(DEFAULT_TOPIC, "http://example.com");

        solo = new Solo(getInstrumentation(), getActivity());

    }

    private void assertCurrentTopic(final String topic) throws InterruptedException {
        Probe topicProbe = new Probe() {
            private CharSequence currentTopic = "";

            @Override
            public boolean isSatisfied() {
                return topic.equals(currentTopic);
            }

            @Override
            public void sample() {
                TextView view = (TextView) solo.getView(org.dandelion.radiot.R.id.current_topic_text);
                this.currentTopic = view.getText();
            }

            @Override
            public void describeAcceptanceCriteriaTo(Description d) {
                d.appendText("Current topic equal to: ").appendValue(topic);
            }

            @Override
            public void describeFailureTo(Description d) {
                d.appendText("Current topic was: ").appendValue(currentTopic);
            }
        };

        assertEventually(topicProbe);
    }
}
