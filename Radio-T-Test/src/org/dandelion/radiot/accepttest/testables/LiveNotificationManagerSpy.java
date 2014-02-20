package org.dandelion.radiot.accepttest.testables;

import org.dandelion.radiot.helpers.async.NotificationTrace;
import org.dandelion.radiot.live.LiveNotificationManager;

import static org.hamcrest.CoreMatchers.equalTo;

public class LiveNotificationManagerSpy extends LiveNotificationManager {
    public static final String HIDDEN = "<hidden>";
    private NotificationTrace<String> foregroundNotifications = new NotificationTrace<String>();
    private NotificationTrace<String> backgroundNotifications = new NotificationTrace<String>();

    public LiveNotificationManagerSpy() {
        super(null, null);
    }

    @Override
    public void hideNotifications() {
        foregroundNotifications.append(HIDDEN);
        backgroundNotifications.append(HIDDEN);
    }

    @Override
    public void showForegroundNote(String text) {
        foregroundNotifications.append(text);
    }

    @Override
    public void showBackgroundNote(String text) {
        backgroundNotifications.append(text);
    }

    public void notificationsHidden() throws InterruptedException {
        foregroundNotifications.receivedNotification(equalTo(HIDDEN));
        backgroundNotifications.receivedNotification(equalTo(HIDDEN));
    }

    public void showsForegroundNotification(String text) throws InterruptedException {
        foregroundNotifications.receivedNotification(equalTo(text));
    }

    public void showsBackgroundNotification(String text) throws InterruptedException {
        backgroundNotifications.receivedNotification(equalTo(text));
    }
}
