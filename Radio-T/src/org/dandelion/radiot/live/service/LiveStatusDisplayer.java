package org.dandelion.radiot.live.service;

import org.dandelion.radiot.live.LiveShowApp;
import org.dandelion.radiot.live.core.LiveShowState;

public class LiveStatusDisplayer {
    private final NotificationBar notificationBar;
    private final String title;
    private final String[] statusLabels;

    LiveStatusDisplayer(NotificationBar notificationBar, String title, String[] statusLabels) {
        this.notificationBar = notificationBar;
        this.title = title;
        this.statusLabels = statusLabels;
    }

    public void updateStatus(LiveShowState state) {
        if (state == LiveShowState.Idle) {
            notificationBar.hideIcon(LiveShowApp.LIVE_NOTIFICATION_ID);
        } else {
            notificationBar.showIcon(
                    LiveShowApp.LIVE_NOTIFICATION_ID,
                    LiveShowApp.LIVE_ICON_RESOURCE_ID,
                    title,
                    statusLabels[0]);
        }
    }
}
