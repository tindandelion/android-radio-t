package org.dandelion.radiot.common.ui;

import android.content.Context;
import com.google.android.gms.analytics.GoogleAnalytics;
import com.google.android.gms.analytics.HitBuilders;
import com.google.android.gms.analytics.Logger;
import com.google.android.gms.analytics.Tracker;
import org.dandelion.radiot.R;

public abstract class ActivityTracker {
    private static final String UI_EVENT = "ui_event";

    private static ActivityTracker instance = null;

    public static ActivityTracker getInstance(Context context) {
        if (instance == null) {
            instance = createTracker(context);
        }
        return instance;
    }

    private static ActivityTracker createTracker(Context context) {
//        if (BuildConfig.DEBUG) {
//            return new NullTracker();
//        } else {
//            return new GooglePlayServicesTracker(context);
//        }
        return new GooglePlayServicesTracker(context);
    }

    public abstract void activityStarted(TrackedActivity activity);

    public abstract void activityStopped(TrackedActivity activity);

    public abstract void trackEvent(String action, String label);

    private static class GooglePlayServicesTracker extends ActivityTracker {
        private final GoogleAnalytics analytics;
        private final Tracker tracker;

        public GooglePlayServicesTracker(Context context) {
            analytics = GoogleAnalytics.getInstance(context);
            analytics.getLogger().setLogLevel(Logger.LogLevel.VERBOSE);

            tracker = analytics.newTracker(R.xml.google_analytics);
        }

        @Override
        public void activityStarted(TrackedActivity activity) {
            analytics.reportActivityStart(activity);
        }

        @Override
        public void activityStopped(TrackedActivity activity) {
            analytics.reportActivityStop(activity);
        }

        @Override
        public void trackEvent(String action, String label) {
            try {
                tracker.send(new HitBuilders.EventBuilder()
                        .setCategory(UI_EVENT)
                        .setAction(action)
                        .setLabel(label)
                        .build());
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
