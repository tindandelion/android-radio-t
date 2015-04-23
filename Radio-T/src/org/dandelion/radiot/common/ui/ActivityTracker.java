package org.dandelion.radiot.common.ui;

import com.google.analytics.tracking.android.EasyTracker;
import org.dandelion.radiot.BuildConfig;

public abstract class ActivityTracker {
    private static final String UI_EVENT = "ui_event";

    private static ActivityTracker instance = null;

    public static ActivityTracker getInstance() {
        if (instance == null) {
            instance = createTracker();
        }
        return instance;
    }

    private static ActivityTracker createTracker() {
        if (BuildConfig.DEBUG) {
            return new NullTracker();
        } else {
            return new GoogleAnalyticsEasyTracker();
        }
    }

    public abstract void activityStarted(TrackedActivity activity);

    public abstract void activityStopped(TrackedActivity activity);

    public abstract void trackEvent(String action, String label);

    private static class GoogleAnalyticsEasyTracker extends ActivityTracker {
        private EasyTracker tracker = EasyTracker.getInstance();

        @Override
        public void activityStarted(TrackedActivity activity) {
            tracker.activityStart(activity);
        }

        @Override
        public void activityStopped(TrackedActivity activity) {
            tracker.activityStop(activity);
        }

        @Override
        public void trackEvent(String action, String label) {
            try {
                EasyTracker.getTracker().sendEvent(UI_EVENT, action, label, null);
            } catch (Exception ignored) {
            }
        }
    }

    private static class NullTracker extends ActivityTracker {
        @Override
        public void activityStarted(TrackedActivity activity) {
        }

        @Override
        public void activityStopped(TrackedActivity activity) {
        }

        @Override
        public void trackEvent(String action, String label) {
        }
    }
}
