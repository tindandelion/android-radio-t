package org.dandelion.radiot.endtoend.live.helpers;

import android.app.Activity;
import android.app.Instrumentation;
import android.view.View;
import android.widget.ImageButton;
import android.widget.TextView;
import com.robotium.solo.Solo;
import org.dandelion.radiot.R;
import org.dandelion.radiot.helpers.async.Probe;
import org.dandelion.radiot.live.schedule.DeterministicScheduler;
import org.hamcrest.Description;

import static org.dandelion.radiot.helpers.async.Poller.assertEventually;

public class CurrentTopicRunner extends Solo {
    private final DeterministicScheduler scheduler;

    public CurrentTopicRunner(Instrumentation instrumentation, Activity activity, DeterministicScheduler scheduler) {
        super(instrumentation, activity);
        this.scheduler = scheduler;
    }

    public void refreshTopic() {
        scheduler.performAction();
    }

    public void closeTopicView() {
        ImageButton button = (ImageButton) getView(R.id.current_topic_hide);
        clickOnView(button);
    }

    public void showsCurrentTopic(final String topic) throws InterruptedException {
        assertEventually(topicViewIsShown(true));
        assertEventually(topicTextEquals(topic));
    }

    public void showsNoTopic() throws InterruptedException {
        assertEventually(topicViewIsShown(false));
    }

    private Probe topicViewIsShown(final boolean desiredState) {
        return new Probe() {
            public boolean isSatisfied = false;


            @Override
            public boolean isSatisfied() {
                return isSatisfied;
            }

            @Override
            public void sample() {
                View view = getView(R.id.current_topic);
                isSatisfied = view.isShown() == desiredState;
            }

            @Override
            public void describeAcceptanceCriteriaTo(Description d) {
                d.appendText("Topic view is ");
                if (desiredState) d.appendText("visible");
                else d.appendText("invisible");
            }

            @Override
            public void describeFailureTo(Description d) {
                d.appendText("Topic view was ");
                if (desiredState) d.appendText("invisible");
                else d.appendText("visible");
            }
        };
    }

    private Probe topicTextEquals(final String topic) {
        return new Probe() {
            private CharSequence currentTopic = "";

            @Override
            public boolean isSatisfied() {
                return topic.equals(currentTopic);
            }

            @Override
            public void sample() {
                TextView textView = (TextView) getView(org.dandelion.radiot.R.id.current_topic_text);
                this.currentTopic = textView.getText();
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
    }

}
