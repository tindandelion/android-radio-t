package org.dandelion.radiot.live.core;

public class LiveShowPlayer implements AudioStream.StateListener, Scheduler.Performer {
    private LiveShowStateHolder stateHolder;
    private AudioStream audioStream;
    private Scheduler scheduler;

    public static interface StateVisitor {
        void onWaiting(long timestamp);
        void onIdle();
        void onConnecting(long timestamp);
        void onPlaying(long timestamp);
        void onStopping(long timestamp);
    }

    public LiveShowPlayer(AudioStream audioStream, LiveShowStateHolder stateHolder, Scheduler scheduler) {
        this.audioStream = audioStream;
        this.stateHolder = stateHolder;
        this.scheduler = scheduler;
        this.audioStream.setStateListener(this);
    }

    public void togglePlayback() {
        currentState().togglePlayback(this);
    }

    @Override
    public void performNextAttempt() {
        beConnecting();
    }

    public void beIdle() {
        setState(LiveShowState.Idle);
        scheduler.cancelAttempts();
    }

    public void beConnecting() {
        try {
            audioStream.play();
            setState(LiveShowState.Connecting);
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
    }

    public void beStopping() {
        audioStream.stop();
        setState(LiveShowState.Stopping);
    }

    public void beWaiting() {
        setState(LiveShowState.Waiting);
        scheduler.scheduleNextAttempt();
    }

    @Override
    public void onStarted() {
        setState(LiveShowState.Playing);
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
        stateHolder.setValue(state, currentTimestamp());
    }

    private long currentTimestamp() {
        return System.currentTimeMillis();
    }
}
