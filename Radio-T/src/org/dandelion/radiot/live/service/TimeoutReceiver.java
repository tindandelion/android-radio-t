package org.dandelion.radiot.live.service;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;

public class TimeoutReceiver extends BroadcastReceiver {
    @Override
    public void onReceive(Context context, Intent intent) {
        signalToLiveShowService(context);
    }

    private void signalToLiveShowService(Context context) {
        Intent intent = new Intent(context, LiveShowService.class);
        intent.setAction(LiveShowService.TIMEOUT_ACTION);
        context.startService(intent);
    }
}
