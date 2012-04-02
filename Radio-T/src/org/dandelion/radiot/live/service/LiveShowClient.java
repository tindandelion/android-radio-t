package org.dandelion.radiot.live.service;

import android.content.ComponentName;
import android.content.Context;
import android.content.Intent;
import android.content.ServiceConnection;
import android.os.IBinder;
import org.dandelion.radiot.live.core.LiveShowStateHolder;
import org.dandelion.radiot.live.core.LiveShowStateListener;

public class LiveShowClient {
    private LiveShowStateHolder stateHolder;
    private Context context;

    private ServiceConnection onService = new ServiceConnection() {
        public void onServiceDisconnected(ComponentName name) {
        }

        public void onServiceConnected(ComponentName name, IBinder binder) {
        }
    };

    public LiveShowClient(Context context, LiveShowStateHolder stateHolder, LiveShowStateListener listener) {
        this.context = context;
        this.stateHolder = stateHolder;
        startTrackingState(listener);
        bindToService();
    }

    private void startTrackingState(LiveShowStateListener listener) {
        stateHolder.setListener(listener);
    }

    public void release() {
        context.unbindService(onService);
        stopTrackingState();
    }

    private void stopTrackingState() {
        stateHolder.setListener(null);
    }

    private void bindToService() {
        Intent i = new Intent(context, LiveShowService.class);
        context.startService(i);
        context.bindService(i, onService, 0);
    }

    public void togglePlayback() {
        Intent intent = new Intent(context, LiveShowService.class);
        intent.setAction(LiveShowService.TOGGLE_ACTION);
        context.startService(intent);
    }
}
