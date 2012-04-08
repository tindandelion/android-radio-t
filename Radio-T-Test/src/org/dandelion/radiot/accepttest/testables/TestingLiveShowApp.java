package org.dandelion.radiot.accepttest.testables;

import android.content.Context;
import android.content.Intent;
import org.dandelion.radiot.live.LiveShowApp;
import org.dandelion.radiot.live.core.AudioStream;
import org.dandelion.radiot.live.service.LiveShowService;
import org.dandelion.radiot.live.service.NotificationBar;

import java.io.IOException;

public class TestingLiveShowApp extends LiveShowApp {
    private AudioStream audioStream;
    private String audioUrl = "";
    private NotificationBar notificationBar;

    public TestingLiveShowApp(NotificationBar notificationBar) {
        this.notificationBar = notificationBar;
    }

    @Override
    public AudioStream createAudioStream() {
        audioStream = new AudioStream(null) {
            @Override
            public void play() throws IOException {
                super.playUrl(audioUrl);
            }

            @Override
            public void release() {
                super.release();
                audioStream = null;
            }
        };
        return audioStream;
    }

    @Override
    public NotificationBar createNotificatioBar(Context context) {
        return notificationBar;
    }

    public void setAudioUrl(String value) {
        this.audioUrl = value;
    }

    public void signalWaitTimeout(Context context) {
        Intent intent = new Intent(LiveShowService.TIMEOUT_ACTION);
        context.sendBroadcast(intent);
    }
}
