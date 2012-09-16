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
    private LiveShowRunner runner;
    private TestableMediaPlayerStream liveShowStream;

    public void testStartStopPlayback() throws Exception {
        liveShowStream.activateTranslation();

        runner.startTranslation();
        runner.showsTranslationInProgress();

        runner.stopTranslation();
        runner.showsTranslationStopped();

        runner.startTranslation();
        runner.showsTranslationInProgress();
    }

    public void testTryToReconnectContinuouslyInWaitingMode() throws Exception {
        liveShowStream.suppressTranslation();
        runner.startTranslation();
        runner.showsWaiting();

        liveShowStream.activateTranslation();
        signalWaitTimeout();
        runner.showsTranslationInProgress();
    }

    public void testStopWaiting() throws Exception {
        liveShowStream.suppressTranslation();

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
        FakeStatusDisplayer statusDisplayer = new FakeStatusDisplayer();
        setupTestingApp(statusDisplayer);
        runner = new LiveShowRunner(getInstrumentation(), getActivity(), statusDisplayer);
    }

    @Override
    protected void tearDown() throws Exception {
        runner.finish();
        liveShowStream.doRelease();
        super.tearDown();
    }

    private void setupTestingApp(final LiveShowStateListener statusDisplayer) {
        getInstrumentation().runOnMainSync(new Runnable() {
            @Override
            public void run() {
                liveShowStream = new TestableMediaPlayerStream();
                LiveShowApp.setTestingInstance(new LiveShowApp() {
                    @Override
                    public AudioStream createAudioStream() {
                        return liveShowStream;
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
