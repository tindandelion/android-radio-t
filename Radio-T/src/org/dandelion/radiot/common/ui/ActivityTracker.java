package org.dandelion.radiot.common.ui;

import com.google.analytics.tracking.android.EasyTracker;
import org.dandelion.radiot.BuildConfig;

public abstract class ActivityTracker {
    public static ActivityTracker create() {
        if (BuildConfig.DEBUG) {
            return new NullTracker();
        } else {
            return new GoogleAnalyticsTracker();
        }
    }

    public abstract void activityStarted(TrackedActivity activity);
    public abstract void activityStopped(TrackedActivity activity);

    private static class GoogleAnalyticsTracker extends ActivityTracker {
        public EasyTracker tracker = EasyTracker.getInstance();

        @Override
        public void activityStarted(TrackedActivity activity) {
            tracker.activityStart(activity);
        }

        @Override
        public void activityStopped(TrackedActivity activity) {
            tracker.activityStop(activity);
        }
    }

    private static class NullTracker extends ActivityTracker {
        @Override
        public void activityStarted(TrackedActivity activity) {
        }

        @Override
        public void activityStopped(TrackedActivity activity) {
        }
    }
}
