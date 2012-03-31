package org.dandelion.radiot.live.ui;

import android.content.ComponentName;
import android.content.Context;
import android.content.Intent;
import android.content.ServiceConnection;
import android.os.IBinder;
import org.dandelion.radiot.live.core.LiveShowPlayer;
import org.dandelion.radiot.live.service.LiveShowService;

public class LiveShowServiceClient {
    private LiveShowService service;
    private Runnable serviceConnected;
    private Context context;

    private ServiceConnection onService = new ServiceConnection() {
        public void onServiceDisconnected(ComponentName name) {
        }

        public void onServiceConnected(ComponentName name, IBinder binder) {
            service = ((LiveShowService.LocalBinder) binder).getService();
            serviceConnected.run();
        }
    };

    public LiveShowServiceClient(Context context, Runnable onServiceConnected) {
        this.context = context;
        this.serviceConnected = onServiceConnected;
        bindToService();
    }

    public void release() {
        context.unbindService(onService);
    }

    private void bindToService() {
        Intent i = new Intent(context, LiveShowService.class);
        context.startService(i);
        context.bindService(i, onService, 0);
    }

    public void togglePlayback() {
        service.togglePlayback();
    }

    public void queryState(LiveShowPlayer.StateVisitor visitor) {
        service.queryState(visitor);
    }
}
