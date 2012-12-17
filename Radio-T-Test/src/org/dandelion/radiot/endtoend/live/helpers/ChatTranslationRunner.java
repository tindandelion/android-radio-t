package org.dandelion.radiot.endtoend.live.helpers;

import android.app.Activity;
import android.app.Instrumentation;
import com.jayway.android.robotium.solo.Solo;

import static org.dandelion.radiot.endtoend.live.RobotiumMatchers.showsText;
import static org.hamcrest.MatcherAssert.assertThat;

public class ChatTranslationRunner extends Solo {
    public ChatTranslationRunner(Instrumentation instrumentation, Activity activity) {
        super(instrumentation, activity);
    }

    public void showsChatMessage(String message) {
        assertThat(this, showsText(message));
    }

}
