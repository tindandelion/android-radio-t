package org.dandelion.radiot.live.service;

import android.app.AlarmManager;
import android.app.PendingIntent;
import android.content.*;
import org.dandelion.radiot.live.core.Timeout;

public class AlarmTimeout implements Timeout {
    public Context context;
    private AlarmManager manager;
    private PendingIntent intent;

    public AlarmTimeout(Context context, String actionName) {
        this.context = context;
        this.manager = (AlarmManager) this.context.getSystemService(Context.ALARM_SERVICE);
        this.intent = PendingIntent.getBroadcast(this.context.getApplicationContext(), 0,
                new Intent(actionName), 0);
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
}
