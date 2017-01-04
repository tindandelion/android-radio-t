package org.dandelion.radiot.common.ui;

import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;

public class TrackedActivity extends AppCompatActivity {
    private ActivityTracker tracker;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        tracker = ActivityTracker.getInstance(getApplication());
    }

    @Override
    protected void onStart() {
        super.onStart();
        tracker.activityStarted(this);
    }

    @Override
    protected void onStop() {
        super.onStop();
        tracker.activityStopped(this);
    }

    public void trackEvent(String action, String label) {
        tracker.trackEvent(action, label);
    }
}
