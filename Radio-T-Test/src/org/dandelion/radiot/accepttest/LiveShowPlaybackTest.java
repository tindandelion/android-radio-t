package org.dandelion.radiot.accepttest;

import android.app.AlarmManager;
import android.app.PendingIntent;
import android.content.Context;
import android.content.Intent;
import android.test.ActivityInstrumentationTestCase2;
import org.dandelion.radiot.accepttest.drivers.LiveShowRunner;
import org.dandelion.radiot.accepttest.testables.FakeStatusDisplayer;
import org.dandelion.radiot.accepttest.testables.TestableMediaPlayerStream;
import org.dandelion.radiot.live.LiveShowApp;
import org.dandelion.radiot.live.core.AudioStream;
import org.dandelion.radiot.live.core.LiveShowStateListener;
import org.dandelion.radiot.live.service.LiveShowService;
import org.dandelion.radiot.live.ui.LiveShowActivity;

public class LiveShowPlaybackTest extends
        ActivityInstrumentationTestCase2<LiveShowActivity> {
    private static final String TEST_LIVE_URL = "http://icecast.bigrradio.com/80s90s";

    private final FakeStatusDisplayer statusDisplayer = new FakeStatusDisplayer();
    private LiveShowRunner runner;
    private TestableMediaPlayerStream liveStream;

    public void testStartStopPlayback() throws Exception {
        runner.startTranslation();
        runner.showsTranslationInProgress();

        runner.stopTranslation();
        runner.showsTranslationStopped();

        runner.startTranslation();
        runner.showsTranslationInProgress();
    }

    public void testTryToReconnectContinuouslyInWaitingMode() throws Exception {
        liveStream.url = "http://non-existent";
        runner.startTranslation();
        runner.showsWaiting();

        liveStream.url = TEST_LIVE_URL;
        signalWaitTimeout();
        runner.showsTranslationInProgress();
    }

    public void testStopWaiting() throws Exception {
        liveStream.url = "http://non-existent";

        runner.startTranslation();
        runner.showsWaiting();

        runner.stopTranslation();
        runner.showsStopped();
    }

    public LiveShowPlaybackTest() {
        super(LiveShowActivity.class);
    }

    @Override
    protected void setUp() throws Exception {
        super.setUp();
        setupTestingApp();
        liveStream.url = TEST_LIVE_URL;
        runner = new LiveShowRunner(getInstrumentation(), getActivity(), statusDisplayer);
    }

    @Override
    protected void tearDown() throws Exception {
        runner.finish();
        liveStream.doRelease();
        super.tearDown();
    }

    private void setupTestingApp() {
        getInstrumentation().runOnMainSync(new Runnable() {
            @Override
            public void run() {
                liveStream = new TestableMediaPlayerStream();
                LiveShowApp.setTestingInstance(new LiveShowApp() {
                    @Override
                    public AudioStream createAudioStream() {
                        return liveStream;
                    }

                    @Override
                    public LiveShowStateListener createStatusDisplayer(Context context) {
                        return statusDisplayer;
                    }
                });
            }
        });
    }

    private void signalWaitTimeout() {
        Intent intent = new Intent(LiveShowService.TIMEOUT_ACTION);
        cancelScheduledAlarm(intent);
        fireSimulatedAlarm(intent);
    }

    private void cancelScheduledAlarm(Intent intent) {
        AlarmManager manager = (AlarmManager) context().getSystemService(Context.ALARM_SERVICE);
        manager.cancel(PendingIntent.getBroadcast(context(), 0, intent, 0));
    }

    private void fireSimulatedAlarm(Intent intent) {
        context().sendBroadcast(intent);
    }

    private Context context() {
        return getInstrumentation().getTargetContext().getApplicationContext();
    }
}
