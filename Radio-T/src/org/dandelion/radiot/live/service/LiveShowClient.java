package org.dandelion.radiot.live.service;

import android.content.Context;
import android.content.Intent;
import org.dandelion.radiot.live.core.LiveShowStateHolder;
import org.dandelion.radiot.live.core.LiveShowStateListener;

public class LiveShowClient {
    private LiveShowStateHolder stateHolder;
    private Context context;

    public LiveShowClient(Context context, LiveShowStateHolder stateHolder, LiveShowStateListener listener) {
        this.context = context;
        this.stateHolder = stateHolder;
        startTrackingState(listener);
    }

    private void startTrackingState(LiveShowStateListener listener) {
        stateHolder.setListener(listener);
    }

    public void release() {
        stopTrackingState();
    }

    private void stopTrackingState() {
        stateHolder.setListener(null);
    }

    public void togglePlayback() {
        Intent intent = new Intent(context, LiveShowService.class);
        intent.setAction(LiveShowService.TOGGLE_ACTION);
        context.startService(intent);
    }
}
