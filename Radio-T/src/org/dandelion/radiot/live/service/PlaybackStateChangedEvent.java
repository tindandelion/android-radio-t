package org.dandelion.radiot.live.service;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import org.dandelion.radiot.live.core.states.LiveShowState;

public class PlaybackStateChangedEvent {
    public static final String TAG = PlaybackStateChangedEvent.class.getName();
    public static final String STATE_EXTRA = TAG + ".State";

    public interface Listener {
        void onPlaybackStateChanged(LiveShowState newState);
    }

    public static void send(Context context, LiveShowState state) {
        Intent intent = new Intent(TAG);
        intent.putExtra(STATE_EXTRA, state);
        context.sendBroadcast(intent);
    }
    
    public static Receiver createReceiver(Context context, Listener listener) {
        return new Receiver(context, listener);
    }

    public static class Receiver extends BroadcastReceiver {
        private Context context;
        private Listener listener;

        public Receiver(Context context, Listener listener) {
            this.context = context;
            this.listener = listener;
            this.context.registerReceiver(this, new IntentFilter(TAG));
        }

        public void release() {
            context.unregisterReceiver(this);
        }

        @Override
        public void onReceive(Context context, Intent intent) {
            LiveShowState state = (LiveShowState) intent.getSerializableExtra(STATE_EXTRA);
            listener.onPlaybackStateChanged(state);
        }
    }
}
