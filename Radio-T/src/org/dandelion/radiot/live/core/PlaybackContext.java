package org.dandelion.radiot.live.core;

import org.dandelion.radiot.live.core.states.*;

// TODO: Get rid of service.switchToNewState
public class PlaybackContext implements AudioStream.StateListener {
//	private static String liveShowUrl = "http://radio10.promodeejay.net:8181/stream";
    public static String liveShowUrl = "http://icecast.bigrradio.com/80s90s";

    public PlaybackState.ILiveShowService service;
    private PlaybackState currentState;
    private AudioStream audioStream;

    public PlaybackContext(PlaybackState.ILiveShowService service, AudioStream audioStream) {
        this.audioStream = audioStream;
        this.audioStream.setStateListener(this);
        this.service = service;
        currentState = new Idle(this);
    }

    public static void setLiveShowUrl(String value) {
		liveShowUrl = value;
	}

    public void playerReset() {
        audioStream.reset();
    }


    public void serviceGoForeground(int i) {
        service.goForeground(i);
    }

    public void connect() {
        try {
            audioStream.play(liveShowUrl);
            setState(new Connecting(this));
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
    }

    public void interrupt() {
        audioStream.stop();
        setState(new Stopping(this));
    }

    private void waitForNextAttempt() {
        setState(new Waiting(this));
    }

    private void setState(PlaybackState state) {
        currentState = state;
        service.switchToNewState(currentState);
    }

    @Override
    public void onStarted() {
        setState(new Playing(this));
    }

    @Override
    public void onError() {
        // TODO: That's a hack, but I want to see how far I can go
        if (currentState instanceof Playing) {
            connect();
        } else {
            waitForNextAttempt();
        }
    }

    @Override
    public void onStopped() {
        setState(new Idle(this));
    }

    public void startPlayback() {
        connect();
    }

    public void stopPlayback() {
        interrupt();
    }
}
