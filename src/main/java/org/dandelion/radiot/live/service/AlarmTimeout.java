package org.dandelion.radiot.live.service;

import android.app.AlarmManager;
import android.app.PendingIntent;
import android.content.*;
import org.dandelion.radiot.live.core.Timeout;

public class AlarmTimeout implements Timeout {
    private AlarmManager manager;
    private PendingIntent intent;

    public AlarmTimeout(Context context, String actionName) {
        this.manager = (AlarmManager) context.getSystemService(Context.ALARM_SERVICE);
        this.intent = createPendingIntent(context, actionName);
    }

    @Override
    public void reset() {
        manager.cancel(intent);
    }

    @Override
    public void set(int milliseconds) {
        manager.set(AlarmManager.RTC_WAKEUP, System.currentTimeMillis()
                + milliseconds, intent);
    }

    private static PendingIntent createPendingIntent(Context context, String action) {
        return PendingIntent.getBroadcast(context.getApplicationContext(), 0,
                new Intent(action), 0);
    }
}
