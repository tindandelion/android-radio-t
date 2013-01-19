package org.dandelion.radiot.endtoend.live.helpers;

import android.app.Instrumentation;
import com.jayway.android.robotium.solo.Solo;
import org.dandelion.radiot.live.schedule.DeterministicScheduler;
import org.dandelion.radiot.live.ui.LiveShowActivity;

import static org.dandelion.radiot.endtoend.live.RobotiumMatchers.showsText;
import static org.hamcrest.MatcherAssert.assertThat;

public class ChatTranslationRunner extends Solo {
    private final DeterministicScheduler scheduler;

    public ChatTranslationRunner(Instrumentation instrumentation, LiveShowActivity activity, DeterministicScheduler scheduler) {
        super(instrumentation, activity);
        this.scheduler = scheduler;
    }

    public void refreshChat() {
        scheduler.performAction();
    }

    public void showsChatMessages(String... messages) {
        for (String msg: messages) {
            assertThat(this, showsText(msg));
        }
    }

    public void showsErrorMessage() {
        assertThat(this, showsText("Трансляция чата недоступна"));
    }
}
