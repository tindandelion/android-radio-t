package org.dandelion.radiot.live.service;

import android.app.Notification;
import android.app.Service;

class Foregrounder {
    protected Service service;
    protected int notificationId;
    private boolean isForeground = false;

    public Foregrounder(Service service, int notificationId) {
        this.service = service;
        this.notificationId = notificationId;
    }

    public void startForeground(Notification note) {
        service.startForeground(notificationId, note);
        isForeground = true;
    }
    
    public void stopForeground() {
        if (isForeground) {
            service.stopForeground(true);
            isForeground = false;
        }
    }
}
