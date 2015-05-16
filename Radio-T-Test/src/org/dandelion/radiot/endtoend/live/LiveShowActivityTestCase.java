package org.dandelion.radiot.endtoend.live;

import android.annotation.TargetApi;
import android.os.Build;
import android.test.ActivityInstrumentationTestCase2;
import org.dandelion.radiot.endtoend.live.helpers.NullDataMonitor;
import org.dandelion.radiot.live.ui.ChatTranslationFragment;
import org.dandelion.radiot.live.ui.LiveShowActivity;
import org.dandelion.radiot.live.ui.CurrentTopicFragment;

public class LiveShowActivityTestCase extends ActivityInstrumentationTestCase2<LiveShowActivity> {
    @TargetApi(Build.VERSION_CODES.FROYO)
    public LiveShowActivityTestCase() {
        super(LiveShowActivity.class);
    }

    @Override
    public void setUp() throws Exception {
        super.setUp();
        CurrentTopicFragment.trackerFactory = new NullDataMonitor();
        ChatTranslationFragment.chatFactory = new NullDataMonitor();
    }
}
