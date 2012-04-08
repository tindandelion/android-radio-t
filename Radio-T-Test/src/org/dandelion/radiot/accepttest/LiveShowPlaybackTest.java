package org.dandelion.radiot.accepttest;

import android.content.Context;
import android.test.ActivityInstrumentationTestCase2;
import org.dandelion.radiot.accepttest.drivers.LiveShowRunner;
import org.dandelion.radiot.accepttest.testables.FakeStatusDisplayer;
import org.dandelion.radiot.accepttest.testables.TestingLiveShowApp;
import org.dandelion.radiot.live.LiveShowApp;
import org.dandelion.radiot.live.ui.LiveShowActivity;

public class LiveShowPlaybackTest extends
		ActivityInstrumentationTestCase2<LiveShowActivity> {
	private static final String TEST_LIVE_URL = "http://icecast.bigrradio.com/80s90s";

    private final FakeStatusDisplayer statusDisplayer = new FakeStatusDisplayer();
    private TestingLiveShowApp app;
    private LiveShowRunner runner;

    public LiveShowPlaybackTest() {
		super("org.dandelion.radiot", LiveShowActivity.class);
	}

	@Override
	protected void setUp() throws Exception {
		super.setUp();
        app = new TestingLiveShowApp(statusDisplayer);
        LiveShowApp.setTestingInstance(app);
        app.setAudioUrl(TEST_LIVE_URL);
        runner = new LiveShowRunner(getInstrumentation(), getActivity(), statusDisplayer);
	}
	
	@Override
	protected void tearDown() throws Exception {
        runner.finish();
		super.tearDown();
	}
	
	public void testStartStopPlayback() throws Exception {
        runner.startTranslation();
        runner.showsTranslationInProgress();

        runner.stopTranslation();
        runner.showsTranslationStopped();

        runner.startTranslation();
        runner.showsTranslationInProgress();
	}
	
	public void testTryToReconnectContinuouslyInWaitingMode() throws Exception {
        app.setAudioUrl("http://non-existent");
        runner.startTranslation();
        runner.showsWaiting();

        app.setAudioUrl(TEST_LIVE_URL);
        app.signalWaitTimeout(context());
        runner.showsTranslationInProgress();
	}

    public void testStopWaiting() throws Exception {
        app.setAudioUrl("http://non-existent");

        runner.startTranslation();
        runner.showsWaiting();

        runner.stopTranslation();
        runner.showsStopped();
	}

    private Context context() {
        return getInstrumentation().getTargetContext();
    }
}
