package org.dandelion.radiot.accepttest;

import android.test.ActivityInstrumentationTestCase2;
import org.dandelion.radiot.accepttest.drivers.LiveShowDriver;
import org.dandelion.radiot.accepttest.testables.TestingLiveShowApp;
import org.dandelion.radiot.live.LiveShowApp;
import org.dandelion.radiot.live.ui.LiveShowActivity;

public class LiveShowPlaybackTest extends
		ActivityInstrumentationTestCase2<LiveShowActivity> {
	private static final String TEST_LIVE_URL = "http://icecast.bigrradio.com/80s90s";

    private TestingLiveShowApp app;
    private LiveShowDriver driver;

    public LiveShowPlaybackTest() {
		super("org.dandelion.radiot", LiveShowActivity.class);
	}

	@Override
	protected void setUp() throws Exception {
		super.setUp();
        app = new TestingLiveShowApp(getInstrumentation().getTargetContext());
        LiveShowApp.setTestingInstance(app);
        app.setAudioUrl(TEST_LIVE_URL);
		driver = new LiveShowDriver(getInstrumentation(), getActivity());
	}
	
	@Override
	protected void tearDown() throws Exception {
        app.reset();
        driver.finishOpenedActivities();
		super.tearDown();
	}
	
	public void testStartPlayback() throws Exception {
        driver.startTranslation();
        driver.assertShowsTranslation();
	}
	
	public void testStopPlaybackWhenPressingStop() throws Exception {
        driver.startTranslation();
        driver.assertShowsTranslation();

        driver.stopTranslation();
        driver.assertShowsStopped();
	}
	
	public void testRestartPlaybackAfterExplicitStop() throws Exception {
        driver.startTranslation();
        driver.assertShowsTranslation();

        driver.stopTranslation();
        driver.assertShowsStopped();

        driver.startTranslation();
        driver.assertShowsTranslation();
	}
	
	public void testTryToReconnectContinuouslyInWaitingMode() throws Exception {
        app.setAudioUrl("http://non-existent");
        driver.startTranslation();
        driver.assertShowsWaiting();

        app.setAudioUrl(TEST_LIVE_URL);
        app.signalWaitTimeout();
        driver.assertShowsTranslation();
	}

	
	public void testStopWaiting() throws Exception {
        app.setAudioUrl("http://non-existent");

        driver.startTranslation();
        driver.assertShowsWaiting();

        driver.stopTranslation();
        driver.assertShowsStopped();
	}

}
