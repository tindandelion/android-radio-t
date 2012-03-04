package org.dandelion.radiot.live.core;

import org.dandelion.radiot.live.core.states.*;

// TODO: Get rid of service.switchToNewState
public class PlaybackContext implements AudioStream.StateListener {
    private PlaybackStateListener listener;
    //	private static String liveShowUrl = "http://radio10.promodeejay.net:8181/stream";
    public static String liveShowUrl = "http://icecast.bigrradio.com/80s90s";
    public PlaybackState.ILiveShowService service;
    private PlaybackState state;
    private AudioStream audioStream;
    private Timeout waitTimeout;
    private Runnable onWaitTimeout = new Runnable() {
        @Override
        public void run() {
            beConnecting();
        }
    };

    public void beIdle() {
        waitTimeout.reset();
        setState(new Idle(this));
    }

    public interface PlaybackStateListener {
        void onChangedState(PlaybackState oldState, PlaybackState newState);
    }
    public static interface PlaybackStateVisitor {
        void onWaiting(Waiting state);
        void onIdle(Idle state);
        void onConnecting(Connecting connecting);
        void onPlaying(Playing playing);
        void onStopping(Stopping stopping);
    }

    public PlaybackContext(PlaybackState.ILiveShowService service, AudioStream audioStream, Timeout waitTimeout) {
        this.audioStream = audioStream;
        this.service = service;
        this.waitTimeout = waitTimeout;
        this.state = new Idle(this);
        this.audioStream.setStateListener(this);
    }

    public void setListener(PlaybackStateListener listener) {
        this.listener = listener;
    }

    public PlaybackState getState() {
        return state;
    }
    public boolean isIdle() {
        // TODO: One more instanceof
        return (state instanceof Idle);
    }

    public static void setLiveShowUrl(String value) {
		liveShowUrl = value;
	}

    public void queryState(PlaybackStateVisitor visitor) {
        state.acceptVisitor(visitor);
    }


    public void beConnecting() {
        try {
            audioStream.play(liveShowUrl);
            setState(new Connecting(this));
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
    }

    public void beStopping() {
        audioStream.stop();
        setState(new Stopping(this));
    }

    public void beWaiting() {
        waitTimeout.set(PlaybackState.waitTimeout, onWaitTimeout);
        setState(new Waiting(this));
    }

    private void setState(PlaybackState state) {
        PlaybackState oldState = this.state;
        this.state = state;
        if (listener != null) {
            listener.onChangedState(oldState, this.state);
        }
    }

    @Override
    public void onStarted() {
        setState(new Playing(this));
    }

    @Override
    public void onError() {
        // TODO: That's a hack, but I want to see how far I can go
        if (state instanceof Playing) {
            beConnecting();
        } else {
            beWaiting();
        }
    }

    @Override
    public void onStopped() {
        beIdle();
    }

    public void startPlayback() {
        state.startPlayback();
    }

    public void stopPlayback() {
        state.stopPlayback();
    }
}
