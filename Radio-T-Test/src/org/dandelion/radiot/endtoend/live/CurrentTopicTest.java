package org.dandelion.radiot.endtoend.live;

import com.robotium.solo.Solo;
import org.dandelion.radiot.live.ui.topics.CurrentTopicFragment;
import org.dandelion.radiot.live.ui.topics.TopicListener;
import org.dandelion.radiot.live.ui.topics.TopicTracker;
import org.dandelion.radiot.live.ui.topics.TopicTrackerFactory;

import static org.dandelion.radiot.endtoend.live.RobotiumMatchers.showsText;
import static org.hamcrest.MatcherAssert.assertThat;


public class CurrentTopicTest extends LiveShowActivityTestCase {
    public static final String DEFAULT_TOPIC = "What is a Web Framework?";

    private FakeTopicTrackerClient client = new FakeTopicTrackerClient();
    private Solo solo;

    public void testShowsCurrentTopic() throws Exception {
        assertThat(solo, showsText(DEFAULT_TOPIC));
    }

    public void testWhenTopicChanges_refreshView() throws Exception {
        final String newTopic = "Amazon's ginormous public cloud turns 8 today";
        client.changeTopic(newTopic);
        assertThat(solo, showsText(newTopic));
    }

    @Override
    public void setUp() throws Exception {
        super.setUp();
        CurrentTopicFragment.trackerFactory = client;

        solo = new Solo(getInstrumentation(), getActivity());
    }

    private class FakeTopicTrackerClient implements TopicTracker, TopicTrackerFactory {
        private TopicListener listener;

        public void changeTopic(final String newTopic) {
            if (listener != null) {
                getActivity().runOnUiThread(new Runnable() {
                    @Override
                    public void run() {
                        listener.onTopicChanged(newTopic);
                    }
                });
            }
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
