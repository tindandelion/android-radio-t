package org.dandelion.radiot.accepttest.testables;

import android.content.Context;
import android.content.Intent;
import org.dandelion.radiot.live.LiveShowApp;
import org.dandelion.radiot.live.core.AudioStream;
import org.dandelion.radiot.live.service.LiveShowService;
import org.dandelion.radiot.live.ui.LiveStatusDisplayer;

import java.io.IOException;

public class TestingLiveShowApp extends LiveShowApp {
    private AudioStream audioStream;
    private String audioUrl = "";
    private LiveStatusDisplayer statusDisplayer;

    public TestingLiveShowApp(LiveStatusDisplayer statusDisplayer) {
        this.statusDisplayer = statusDisplayer;
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

    public void setAudioUrl(String value) {
        this.audioUrl = value;
    }

    public void signalWaitTimeout(Context context) {
        Intent intent = new Intent(LiveShowService.TIMEOUT_ACTION);
        context.sendBroadcast(intent);
    }

    @Override
    public LiveStatusDisplayer createStatusDisplayer(Context context) {
        return statusDisplayer;
    }
}
