package org.dandelion.radiot.common.ui;

import android.os.Bundle;
import android.support.v4.app.FragmentActivity;
import com.google.analytics.tracking.android.EasyTracker;

public class TrackedActivity extends FragmentActivity {
    private EasyTracker tracker;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        tracker = EasyTracker.getInstance();
    }

    @Override
    protected void onStart() {
        super.onStart();
        tracker.activityStart(this);
    }

    @Override
    protected void onStop() {
        super.onStop();
        tracker.activityStop(this);
    }
}
