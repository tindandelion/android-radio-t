package org.dandelion.radiot.live.core;

public class LiveShowPlayer implements AudioStream.Listener, Scheduler.Performer {
    private LiveShowStateHolder stateHolder;
    private AudioStream audioStream;
    private Scheduler scheduler;
    private PlayerActivityListener activityListener;

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
        notifyActivityListener();
    }

    private void notifyActivityListener() {
        if (null != activityListener) {
            activityListener.onDeactivated();
        }
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

    public void setActivityListener(PlayerActivityListener listener) {
        this.activityListener = listener;
    }
}
