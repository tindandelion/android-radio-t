package org.dandelion.radiot.live;

import android.content.Context;
import android.content.Intent;
import org.dandelion.radiot.live.ui.LiveShowActivity;
import org.dandelion.radiot.util.IconNote;

public class LiveShowNote extends IconNote {
    private Intent intent;

    public LiveShowNote(Context context, int notificationId) {
        super(context, notificationId);
    }

    public IconNote showsActivity(Class<LiveShowActivity> activityClass) {
        intent = new Intent(context, activityClass);
        return this;
    }

    @Override
    protected Intent activityIntent() {
        return intent;
    }
}
