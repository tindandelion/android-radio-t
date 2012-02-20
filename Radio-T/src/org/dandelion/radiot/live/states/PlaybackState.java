package org.dandelion.radiot.live.states;

import android.media.MediaPlayer;

public class PlaybackState {
    protected static String liveShowUrl = "http://radio10.promodeejay.net:8181/stream";
    //	private static String liveShowUrl = "http://icecast.bigrradio.com/80s90s";
    protected static int waitTimeout = 60 * 1000;

    public static void setLiveShowUrl(String value) {
        liveShowUrl = value;
    }

    public static void setWaitTimeoutSeconds(int value) {
        waitTimeout = value * 1000;
    }

    protected MediaPlayer player;
    protected PlaybackService service;
    private long timestamp;

    public PlaybackState(MediaPlayer player, PlaybackService service) {
        this.service = service;
        this.player = player;
        this.timestamp = System.currentTimeMillis();
    }

    public void enter() {
    }

    public void leave() {
    }

    public void acceptVisitor(PlaybackStateVisitor visitor) {
    }

    public void onTimeout() {
    }

    public void stopPlayback() {
        service.switchToNewState(new Stopping(player, service));
    }

    public void startPlayback() {
    }

    public long getTimestamp() {
        return timestamp;
    }

}
