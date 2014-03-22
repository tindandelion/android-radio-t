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
    private static final String NEW_TOPIC = "Amazon's ginormous public cloud turns 8 today";

    private FakeTopicTrackerClient client = new FakeTopicTrackerClient();

    public void testShowsCurrentTopic() throws Exception {
        Solo solo = new Solo(getInstrumentation(), getActivity());
        assertThat(solo, showsText(DEFAULT_TOPIC));
    }

    public void testWhenTopicChanges_refreshView() throws Exception {
        Solo solo = new Solo(getInstrumentation(), getActivity());
        client.changeTopic(NEW_TOPIC);
        assertThat(solo, showsText(NEW_TOPIC));
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

    private static class FakeTopicTrackerClient {
        public void changeTopic(String newTopic) {

        }
    }
}
