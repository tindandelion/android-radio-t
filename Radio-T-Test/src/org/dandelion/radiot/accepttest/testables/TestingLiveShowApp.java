package org.dandelion.radiot.accepttest.testables;

import android.content.Context;
import android.content.Intent;
import org.dandelion.radiot.live.LiveShowApp;
import org.dandelion.radiot.live.core.AudioStream;
import org.dandelion.radiot.live.service.LiveShowService;
import org.dandelion.radiot.live.ui.LiveStatusDisplayer;

public class TestingLiveShowApp extends LiveShowApp {
    private LiveStatusDisplayer statusDisplayer;
    private TestableAudioStream audioStream = new TestableAudioStream();

    public TestingLiveShowApp(LiveStatusDisplayer statusDisplayer) {
        this.statusDisplayer = statusDisplayer;
    }

    @Override
    public AudioStream createAudioStream() {
        return audioStream;
    }

    public void setAudioUrl(String value) {
        audioStream.url = value;
    }

    public void signalWaitTimeout(Context context) {
        Intent intent = new Intent(LiveShowService.TIMEOUT_ACTION);
        context.sendBroadcast(intent);
    }

    @Override
    public LiveStatusDisplayer createStatusDisplayer(Context context) {
        return statusDisplayer;
    }

    public void finish() {
        audioStream.doRelease();
    }
}
