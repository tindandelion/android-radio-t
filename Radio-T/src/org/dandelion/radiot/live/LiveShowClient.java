package org.dandelion.radiot.live;

import android.content.Context;
import android.content.Intent;
import org.dandelion.radiot.live.core.LiveShowStateHolder;
import org.dandelion.radiot.live.core.LiveShowStateListener;
import org.dandelion.radiot.live.service.LiveShowService;

public class LiveShowClient {
    private LiveShowStateHolder stateHolder;
    private Context context;

    public LiveShowClient(Context context, LiveShowStateHolder stateHolder) {
        this.context = context;
        this.stateHolder = stateHolder;
    }

    public void togglePlayback() {
        Intent intent = new Intent(context, LiveShowService.class);
        intent.setAction(LiveShowService.TOGGLE_ACTION);
        context.startService(intent);
    }

    public void addListener(LiveShowStateListener listener) {
        stateHolder.addListener(listener);
    }

    public void removeListener(LiveShowStateListener listener) {
        stateHolder.removeListener(listener);
    }
}
