package org.dandelion.radiot.live.ui;

import android.content.Context;
import org.dandelion.radiot.R;
import org.dandelion.radiot.live.core.LiveShowState;
import org.dandelion.radiot.util.IconNote;

public class NotificationStatusDisplayer implements LiveStatusDisplayer {
    private IconNote note;
    private String[] labels;

    public NotificationStatusDisplayer(Context context, int noteId) {
        this.labels = context.getResources().getStringArray(R.array.live_show_notification_labels);
        this.note = new IconNote(context, noteId) {{
            setTitle(context.getString(R.string.app_name));
            setIcon(R.drawable.stat_live);
            showsActivity(LiveShowActivity.class);
            beOngoing();
        }};
    }

    @Override
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
        return labels[state.ordinal() - 1];
    }
}
