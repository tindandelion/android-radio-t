package org.dandelion.radiot.live.core;

import org.dandelion.radiot.live.core.states.*;

public class LiveShowPlayer implements AudioStream.StateListener {
    public static int WAIT_TIMEOUT = 60 * 1000;

    private StateChangeListener listener;
    private LiveShowStateHolder stateHolder;
    private AudioStream audioStream;
    private Timeout waitTimeout;
    private Runnable onWaitTimeout = new Runnable() {
        @Override
        public void run() {
            beConnecting();
        }
    };

    public interface StateChangeListener {
        void onChangedState(LiveShowState newState);
    }

    public static interface StateVisitor {
        void onWaiting(long timestamp);
        void onIdle();
        void onConnecting(long timestamp);
        void onPlaying(long timestamp);
        void onStopping(long timestamp);
    }

    public LiveShowPlayer(AudioStream audioStream, LiveShowStateHolder stateHolder, Timeout waitTimeout) {
        this.audioStream = audioStream;
        this.waitTimeout = waitTimeout;
        this.stateHolder = stateHolder;
        this.audioStream.setStateListener(this);
    }

    public void setListener(StateChangeListener listener) {
        this.listener = listener;
    }

    public void queryState(StateVisitor visitor) {
        currentState().acceptVisitor(visitor);
    }

    public void togglePlayback() {
        currentState().togglePlayback(this);
    }

    public boolean isIdle() {
        return (currentState() instanceof Idle);
    }

    public void beIdle() {
        waitTimeout.reset();
        setState(new Idle());
    }

    public void beConnecting() {
        try {
            audioStream.play();
            setState(new Connecting());
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
    }

    public void beStopping() {
        audioStream.stop();
        setState(new Stopping());
    }

    public void beWaiting() {
        waitTimeout.set(WAIT_TIMEOUT, onWaitTimeout);
        setState(new Waiting());
    }

    @Override
    public void onStarted() {
        setState(new Playing());
    }

    @Override
    public void onError() {
        currentState().handleError(this);
    }

    @Override
    public void onStopped() {
        beIdle();
    }

    private LiveShowState currentState() {
        return stateHolder.value();
    }

    private void setState(LiveShowState state) {
        stateHolder.setValue(state);
        if (listener != null) {
            listener.onChangedState(state);
        }
    }
}
