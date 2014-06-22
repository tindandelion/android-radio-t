package org.dandelion.radiot.endtoend.live.helpers;

import android.app.Activity;
import android.app.Instrumentation;
import android.widget.TextView;
import com.robotium.solo.Solo;
import org.dandelion.radiot.helpers.async.Probe;
import org.dandelion.radiot.live.topics.HttpTopicTrackerClient;
import org.hamcrest.Description;

import static org.dandelion.radiot.helpers.async.Poller.assertEventually;

public class CurrentTopicRunner extends Solo {
    private final HttpTopicTrackerClient trackerClient;

    public CurrentTopicRunner(Instrumentation instrumentation, Activity activity, HttpTopicTrackerClient trackerClient) {
        super(instrumentation, activity);
        this.trackerClient = trackerClient;
    }

    public void refreshTopic() {
        trackerClient.refreshTopic();
    }

    public void showsCurrentTopic(final String topic) throws InterruptedException {
        Probe topicProbe = new Probe() {
            private CharSequence currentTopic = "";

            @Override
            public boolean isSatisfied() {
                return topic.equals(currentTopic);
            }

            @Override
            public void sample() {
                TextView view = (TextView) getView(org.dandelion.radiot.R.id.current_topic_text);
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
