package org.dandelion.radiot.live;

import org.dandelion.radiot.R;
import android.content.Context;
import org.dandelion.radiot.live.core.LiveShowState;
import org.dandelion.radiot.live.service.LiveStatusDisplayer;
import org.dandelion.radiot.live.ui.LiveShowActivity;
import org.dandelion.radiot.util.IconNote;

public class NotificationStatusDisplayer implements LiveStatusDisplayer {
    private IconNote note;
    private String[] labels;

    public NotificationStatusDisplayer(Context context, int noteId) {
        this.note = new LiveShowNote(context, noteId)
                .showsActivity(LiveShowActivity.class)
                .setTitle(context.getString(R.string.app_name))
                .setIcon(R.drawable.stat_live)
                .beOngoing();

        this.labels = context.getResources().getStringArray(R.array.live_show_notification_labels);

    }

    @Override
    public void showStatus(LiveShowState state) {
        if (LiveShowState.isIdle(state)) {
            note.hide();
        } else {
            note.setText(getTextForState(state));
            note.show();
        }
    }

    private String getTextForState(LiveShowState state) {
        return labels[state.ordinal() - 1];
    }
}
