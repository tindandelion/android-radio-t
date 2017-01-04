package org.dandelion.radiot.live.service;

import android.app.Service;
import android.content.Context;
import android.content.Intent;
import android.os.IBinder;
import android.os.PowerManager;

public class WakefulService extends Service {
    private static final String TAG = WakefulService.class.getName() + ".WakeLock";

    private static PowerManager.WakeLock wakeLock;

    public static void performWakefulAction(Context context, Class<? extends WakefulService> serviceCls, String action) {
        acquireWakeLock(context);
        sendActionIntent(context, serviceCls, action);
    }

    private static void sendActionIntent(Context context, Class<? extends WakefulService> serviceCls, String action) {
        context.startService(createIntent(context, serviceCls, action));
    }

    protected static Intent createIntent(Context context, Class<? extends WakefulService> serviceCls, String action) {
        Intent intent = new Intent(context, serviceCls);
        intent.setAction(action);
        return intent;
    }

    private static void acquireWakeLock(Context context) {
        getWakeLock(context).acquire();
    }

    private void releaseWakeLock(Context context) {
        getWakeLock(context).release();
    }

    private static PowerManager.WakeLock getWakeLock(Context context) {
        if (wakeLock == null) {
            wakeLock = createNonCountableWakeLock(context);
        }
        return wakeLock;
    }

    private static PowerManager.WakeLock createNonCountableWakeLock(Context context) {
        PowerManager pm = (PowerManager) context.getApplicationContext()
                .getSystemService(Context.POWER_SERVICE);
        PowerManager.WakeLock l = pm.newWakeLock(PowerManager.PARTIAL_WAKE_LOCK, TAG);
        l.setReferenceCounted(false);
        return l;
    }

    @Override
    public IBinder onBind(Intent intent) {
        return null;
    }

    @Override
    public void onCreate() {
        super.onCreate();
        acquireWakeLock(this);
    }

    @Override
    public void onDestroy() {
        releaseWakeLock(this);
        super.onDestroy();
    }
}
