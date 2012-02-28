package org.dandelion.radiot.live.service;

import android.app.AlarmManager;
import android.app.PendingIntent;
import android.content.*;
import org.dandelion.radiot.live.core.Timeout;

public class AlarmTimeout implements Timeout {
    public Context context;
    private AlarmManager manager;
    private PendingIntent intent;
    private Runnable onAlarm;
    private BroadcastReceiver alarmReceiver = new BroadcastReceiver() {
        @Override
        public void onReceive(Context context, Intent intent) {
            if (onAlarm != null) {
                onAlarm.run();
            }
        }
    };

    public AlarmTimeout(Context context, String actionName) {
        this.context = context;
        this.manager = (AlarmManager) this.context.getSystemService(Context.ALARM_SERVICE);
        this.intent = PendingIntent.getBroadcast(this.context.getApplicationContext(), 0,
                new Intent(actionName), 0);
        this.context.registerReceiver(alarmReceiver, new IntentFilter(actionName));
    }

    @Override
    public void reset() {
        manager.cancel(intent);
        onAlarm = null;
    }

    @Override
    public void set(int milliseconds, Runnable action) {
        this.onAlarm = action;
        manager.set(AlarmManager.RTC_WAKEUP, System.currentTimeMillis()
                + milliseconds, intent);
    }

    @Override
    public void release() {
        context.unregisterReceiver(alarmReceiver);
    }
}
