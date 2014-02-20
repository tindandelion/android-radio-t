package org.dandelion.radiot.live;

import org.dandelion.radiot.util.IconNote;

public class LiveNotificationManager {

    private final IconNote foregroundNote;
    private final IconNote backgroundNote;

    public LiveNotificationManager(IconNote foregroundNote, IconNote backgroundNote) {
        this.foregroundNote = foregroundNote;
        this.backgroundNote = backgroundNote;
    }


    public void hideNotifications() {
        foregroundNote.hide();
        backgroundNote.hide();
    }

    public void showForegroundNote(String text) {
        updateNotifications(foregroundNote, backgroundNote, text);
    }

    public void showBackgroundNote(String text) {
        updateNotifications(backgroundNote, foregroundNote, text);
    }

    private void updateNotifications(IconNote active, IconNote inactive, String text) {
        inactive.hide();
        active
                .setTicker(text)
                .setText(text)
                .show();
    }
}
