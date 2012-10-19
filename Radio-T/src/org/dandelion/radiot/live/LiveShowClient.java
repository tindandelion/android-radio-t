package org.dandelion.radiot.live;

import android.content.Context;
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
        LiveShowService.sendTogglePlayback(context);
    }

    public void addListener(LiveShowStateListener listener) {
        stateHolder.addListener(listener);
    }

    public void removeListener(LiveShowStateListener listener) {
        stateHolder.removeListener(listener);
    }
}
