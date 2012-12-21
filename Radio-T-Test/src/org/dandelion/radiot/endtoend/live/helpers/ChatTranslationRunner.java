package org.dandelion.radiot.endtoend.live.helpers;

import android.app.Instrumentation;
import com.jayway.android.robotium.solo.Solo;
import org.dandelion.radiot.R;
import org.dandelion.radiot.live.ui.ChatTranslationFragment;
import org.dandelion.radiot.live.ui.LiveShowActivity;

import static org.dandelion.radiot.endtoend.live.RobotiumMatchers.showsText;
import static org.hamcrest.MatcherAssert.assertThat;

public class ChatTranslationRunner extends Solo {
    private final LiveShowActivity activity;

    public ChatTranslationRunner(Instrumentation instrumentation, LiveShowActivity activity) {
        super(instrumentation, activity);
        this.activity = activity;
    }

    public void showsChatMessage(String message) {
        assertThat(this, showsText(message));
    }

    public void refreshChat() {
        ChatTranslationFragment fragment = findTranslationFragment();
        fragment.refreshChat();
    }

    private ChatTranslationFragment findTranslationFragment() {
        return (ChatTranslationFragment) activity.getSupportFragmentManager().findFragmentById(R.id.chat_translation);
    }
}
