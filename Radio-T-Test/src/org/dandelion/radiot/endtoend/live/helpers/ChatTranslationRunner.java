package org.dandelion.radiot.endtoend.live.helpers;

import android.app.Instrumentation;
import com.jayway.android.robotium.solo.Solo;
import org.dandelion.radiot.live.chat.HttpChatTranslation;
import org.dandelion.radiot.live.ui.LiveShowActivity;

import static org.dandelion.radiot.endtoend.live.RobotiumMatchers.showsText;
import static org.hamcrest.MatcherAssert.assertThat;

public class ChatTranslationRunner extends Solo {
    private final HttpChatTranslation translation;

    public ChatTranslationRunner(Instrumentation instrumentation, LiveShowActivity activity, HttpChatTranslation translation) {
        super(instrumentation, activity);
        this.translation = translation;
    }

    public void showsChatMessage(String message) {
        assertThat(this, showsText(message));
    }

    public void refreshChat() {
        translation.refresh();
    }
}
