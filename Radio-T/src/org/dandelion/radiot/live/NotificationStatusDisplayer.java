package org.dandelion.radiot.live;

import org.dandelion.radiot.live.core.LiveShowState;
import org.dandelion.radiot.live.core.LiveShowStateListener;

public class NotificationStatusDisplayer implements LiveShowStateListener {
    private final String[] labels;
    private final LiveNotificationManager notificationManager;

    public NotificationStatusDisplayer(LiveNotificationManager notificationManager, String[] stateLabels) {
        this.labels = stateLabels;
        this.notificationManager = notificationManager;
    }

    private String textForState(LiveShowState state) {
        return labels[state.ordinal()-1];
    }

    @Override
    public void onStateChanged(LiveShowState state, long timestamp) {
        if (LiveShowState.isIdle(state)) {
            notificationManager.hideNotifications();
        } else {
            String text = textForState(state);
            if (LiveShowState.isActive(state)) {
                notificationManager.showForegroundNote(text);
            } else {
                notificationManager.showBackgroundNote(text);
            }
        }
    }
}
