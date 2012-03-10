package org.dandelion.radiot.live.service;

import android.app.Notification;
import android.app.NotificationManager;
import android.app.Service;
import android.content.Context;

@SuppressWarnings("rawtypes")
abstract class Foregrounder {
	private static final Class[] SIGNATURE = new Class[] { int.class, Notification.class };
    protected Service service;
    protected int notificationId;
    private boolean isForeground;

    public static Foregrounder create(Service service, int notificationId) {
        if (isNewApi()) {
            return new Foregrounder20(service, notificationId);
        } else {
            return new Foregrounder15(service, notificationId);
        }
    }

    private static boolean isNewApi() {
        try {
            Service.class.getMethod("startForeground", SIGNATURE);
            return true;
        } catch (NoSuchMethodException e) {
            return false;
        }
    }

    private Foregrounder(Service service, int notificationId) {
        this.service = service;
        this.notificationId = notificationId;
        this.isForeground = false;
    }

    public void startForeground(Notification n) {
        _startForeground(n);
        isForeground = true;
    }
    
    public void stopForeground() {
        if (isForeground) {
            _stopForeground();
        }
    }

    protected abstract void _stopForeground();
    protected abstract void _startForeground(Notification notification);

    private static class Foregrounder20 extends Foregrounder {
        private Foregrounder20(Service service, int notificationId) {
            super(service, notificationId);
        }

        @Override
        protected void _stopForeground() {
            service.stopForeground(true);
        }

        @Override
        protected void _startForeground(Notification notification) {
            service.startForeground(notificationId, notification);
        }
    }

    private static class Foregrounder15 extends Foregrounder {
        private NotificationManager nm;

        private Foregrounder15(Service service, int notificationId) {
            super(service, notificationId);
            nm = (NotificationManager)service.getSystemService(Context.NOTIFICATION_SERVICE);
        }

        @Override
        protected void _stopForeground() {
            nm.cancel(notificationId);
            service.setForeground(false);
        }

        @Override
        protected void _startForeground(Notification notification) {
            service.setForeground(true);
            nm.notify(notificationId, notification);
        }
    }
}
