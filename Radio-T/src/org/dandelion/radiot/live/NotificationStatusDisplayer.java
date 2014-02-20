package org.dandelion.radiot.live;

import org.dandelion.radiot.live.core.LiveShowState;
import org.dandelion.radiot.live.core.LiveShowStateListener;
import org.dandelion.radiot.util.IconNote;

public class NotificationStatusDisplayer implements LiveShowStateListener {
    private IconNote note;
    private String[] labels;

    public NotificationStatusDisplayer(IconNote note, String[] stateLabels) {
        this.labels = stateLabels;
        this.note = note;
    }

    public void showStatus(LiveShowState state) {
        if (LiveShowState.isIdle(state)) {
            note.hide();
        } else {
            String text = textForState(state);
            note.setTicker(text)
                    .setText(text)
                    .show();
        }
    }

    private String textForState(LiveShowState state) {
        return labels[state.ordinal()-1];
    }

    @Override
    public void onStateChanged(LiveShowState state, long timestamp) {
        showStatus(state);
    }
}
