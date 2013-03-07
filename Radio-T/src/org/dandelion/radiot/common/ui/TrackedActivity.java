package org.dandelion.radiot.common.ui;

import android.support.v4.app.FragmentActivity;
import com.google.analytics.tracking.android.EasyTracker;

public class TrackedActivity extends FragmentActivity {
    @Override
    protected void onStart() {
        super.onStart();
        EasyTracker.getInstance().activityStart(this);
    }

    @Override
    protected void onStop() {
        super.onStop();
        EasyTracker.getInstance().activityStop(this);
    }
}
