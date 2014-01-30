package org.dandelion.radiot.live;

import android.app.Service;
import org.dandelion.radiot.R;
import org.dandelion.radiot.live.core.LiveShowState;
import org.dandelion.radiot.live.core.LiveShowStateListener;
import org.dandelion.radiot.live.ui.LiveShowActivity;
import org.dandelion.radiot.util.IconNote;

public class LiveShowStatusDisplayer implements LiveShowStateListener {
    private static final int LIVE_NOTE_ID = 1;
    private final Service service;
    private final IconNote note;
    private final String[] labels;

    public LiveShowStatusDisplayer(Service service) {
        this.service = service;
        this.labels = service.getResources().getStringArray(R.array.live_show_notification_labels);
        this.note = createNote();
    }

    private IconNote createNote() {
        return new IconNote(service, LIVE_NOTE_ID) {{
            setTitle(context.getString(R.string.app_name));
            setIcon(R.drawable.stat_live);
            showsActivity(LiveShowActivity.class);
            beOngoing();
        }};
    }


    private String textForState(LiveShowState state) {
        return labels[state.ordinal()-1];
    }

    @Override
    public void onStateChanged(LiveShowState state, long timestamp) {
        manageForegroundState(state);
        updateNotification(state);
    }

    private void manageForegroundState(LiveShowState state) {
        if (state == LiveShowState.Connecting) {
            service.startForeground(note.id(), note.build());
        } else if (!LiveShowState.isActive(state)) {
            service.stopForeground(true);
        }
    }

    public void updateNotification(LiveShowState state) {
        if (LiveShowState.isIdle(state)) {
            note.hide();
        } else {
            String text = textForState(state);
            note.setTicker(text)
                    .setText(text)
                    .show();
        }
    }
}
