package org.dandelion.radiot.live.service;

import android.content.Context;
import android.content.Intent;
import org.dandelion.radiot.live.ui.LiveShowActivity;
import org.dandelion.radiot.util.IconNote;

class LiveShowNote extends IconNote {
    public LiveShowNote(Context context, int notificationId) {
        super(context, notificationId);
    }

    @Override
    protected Intent activityIntent() {
        return new Intent(context, LiveShowActivity.class);
    }
}
