package org.dandelion.radiot.live.service;

import android.content.ComponentName;
import android.content.Context;
import android.content.Intent;
import android.content.ServiceConnection;
import android.os.IBinder;
import org.dandelion.radiot.live.core.LiveShowPlayer;
import org.dandelion.radiot.live.core.states.LiveShowState;

public class LiveShowClient {

    public interface StateListener {
        void onStateChanged();
    }

    private PlaybackStateChangedEvent.Receiver eventReceiver;
    private StateListener stateListener;
    private LiveShowService service;
    private Context context;

    private ServiceConnection onService = new ServiceConnection() {
        public void onServiceDisconnected(ComponentName name) {
        }

        public void onServiceConnected(ComponentName name, IBinder binder) {
            service = ((LiveShowService.LocalBinder) binder).getService();
            stateListener.onStateChanged();
        }
    };

    public LiveShowClient(Context context, StateListener listener) {
        this.context = context;
        this.stateListener = listener;
        bindToService();
        startTrackingState();
    }

    private void startTrackingState() {
        PlaybackStateChangedEvent.Listener onStateChanged = new PlaybackStateChangedEvent.Listener() {
            @Override
            public void onPlaybackStateChanged(LiveShowState newState) {
                stateListener.onStateChanged();
            }
        };
        eventReceiver = PlaybackStateChangedEvent.createReceiver(context, onStateChanged);
    }

    public void release() {
        context.unbindService(onService);
        stopTrackingState();
    }

    private void stopTrackingState() {
        eventReceiver.release();
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

    public void queryState(LiveShowPlayer.StateVisitor visitor) {
        service.queryState(visitor);
    }
}
