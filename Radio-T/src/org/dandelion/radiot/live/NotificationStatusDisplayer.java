package org.dandelion.radiot.live;

import org.dandelion.radiot.live.core.LiveShowState;
import org.dandelion.radiot.live.core.LiveShowStateListener;
import org.dandelion.radiot.util.IconNote;

public class NotificationStatusDisplayer implements LiveShowStateListener {
    private final IconNote backgroundNote;
    private final IconNote foregroundNote;
    private final String[] labels;

    public NotificationStatusDisplayer(IconNote foregroundNote, IconNote backgroundNote, String[] stateLabels) {
        this.labels = stateLabels;
        this.foregroundNote = foregroundNote;
        this.backgroundNote = backgroundNote;
    }

    private String textForState(LiveShowState state) {
        return labels[state.ordinal()-1];
    }

    @Override
    public void onStateChanged(LiveShowState state, long timestamp) {
        if (LiveShowState.isIdle(state)) {
            hideAllNotifications();
        } else {
            String text = textForState(state);
            if (LiveShowState.isActive(state)) {
                updateNotifications(foregroundNote, backgroundNote, text);
            } else {
                updateNotifications(backgroundNote, foregroundNote, text);
            }
        }
    }

    private void updateNotifications(IconNote active, IconNote inactive, String text) {
        inactive.hide();
        active
                .setTicker(text)
                .setText(text)
                .show();
    }

    private void hideAllNotifications() {
        foregroundNote.hide();
        backgroundNote.hide();
    }
}
