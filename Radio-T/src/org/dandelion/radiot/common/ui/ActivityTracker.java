package org.dandelion.radiot.common.ui;

import android.content.Context;
import com.google.android.gms.analytics.GoogleAnalytics;
import com.google.android.gms.analytics.HitBuilders;
import com.google.android.gms.analytics.Logger;
import com.google.android.gms.analytics.Tracker;
import org.dandelion.radiot.BuildConfig;
import org.dandelion.radiot.R;

public class ActivityTracker {
    private static final String UI_EVENT = "ui_event";

    private static ActivityTracker instance = null;
    protected final GoogleAnalytics analytics;
    protected final Tracker tracker;

    public static ActivityTracker getInstance(Context context) {
        if (instance == null) {
            instance = new ActivityTracker(context, BuildConfig.DEBUG);
        }
        return instance;
    }

    private ActivityTracker(Context context, boolean debug) {
        analytics = GoogleAnalytics.getInstance(context);
        if (debug) {
            analytics.getLogger().setLogLevel(Logger.LogLevel.VERBOSE);
            analytics.setDryRun(true);
        }
        tracker = analytics.newTracker(R.xml.google_analytics);
    }

    public void activityStarted(TrackedActivity activity) {
        analytics.reportActivityStart(activity);
    }

    public void activityStopped(TrackedActivity activity) {
        analytics.reportActivityStop(activity);
    }

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
