package org.dandelion.radiot.endtoend.live;

import android.annotation.TargetApi;
import android.os.Build;
import android.test.ActivityInstrumentationTestCase2;
import com.robotium.solo.Solo;
import org.dandelion.radiot.live.ui.ChatTranslationFragment;
import org.dandelion.radiot.live.ui.LiveShowActivity;

import static org.dandelion.radiot.endtoend.live.RobotiumMatchers.showsText;
import static org.hamcrest.MatcherAssert.assertThat;


public class CurrentTopicTest extends ActivityInstrumentationTestCase2<LiveShowActivity> {
    public static final String DEFAULT_TOPIC = "What is a Web Framework?";

    public void testShowsCurrentTopic() throws Exception {
        Solo solo = new Solo(getInstrumentation(), getActivity());
        assertThat(solo, showsText(DEFAULT_TOPIC));
    }

    @TargetApi(Build.VERSION_CODES.FROYO)
    public CurrentTopicTest() {
        super(LiveShowActivity.class);
    }

    @Override
    public void setUp() throws Exception {
        super.setUp();
        ChatTranslationFragment.chatFactory = new NullChatTranslation();
    }
}
