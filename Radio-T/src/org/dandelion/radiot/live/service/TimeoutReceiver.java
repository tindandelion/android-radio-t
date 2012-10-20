package org.dandelion.radiot.live.service;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;

public class TimeoutReceiver extends BroadcastReceiver {
    public static final String BROADCAST = "org.dandelion.radiot.live.Timeout";

    @Override
    public void onReceive(Context context, Intent intent) {
        signalToLiveShowService(context);
    }

    private void signalToLiveShowService(Context context) {
        LiveShowService.sendTimeoutElapsed(context);
    }
}
