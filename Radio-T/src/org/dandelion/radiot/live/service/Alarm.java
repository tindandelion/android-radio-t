package org.dandelion.radiot.live.service;

import android.app.AlarmManager;
import android.app.PendingIntent;
import android.content.*;

public class Alarm {
    private static final String TIMEOUT_ELAPSED = "org.dandelion.radiot.live.TimeoutElapsed";

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


    public Alarm(Context context) {
        this.context = context;
        this.manager = (AlarmManager) this.context.getSystemService(Context.ALARM_SERVICE);
        this.intent = PendingIntent.getBroadcast(this.context.getApplicationContext(), 0,
                new Intent(TIMEOUT_ELAPSED), 0);
        this.context.registerReceiver(alarmReceiver, new IntentFilter(TIMEOUT_ELAPSED));
    }

    void reset() {
        manager.cancel(intent);
        onAlarm = null;
    }

    void set(int milliseconds, Runnable onAlarm) {
        this.onAlarm = onAlarm;
        manager.set(AlarmManager.RTC_WAKEUP, System.currentTimeMillis()
                + milliseconds, intent);
    }

    public void release() {
        context.unregisterReceiver(alarmReceiver);
    }
}
