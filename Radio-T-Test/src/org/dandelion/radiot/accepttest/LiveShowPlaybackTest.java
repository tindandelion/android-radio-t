package org.dandelion.radiot.accepttest;

import android.test.ActivityInstrumentationTestCase2;
import org.dandelion.radiot.accepttest.drivers.LiveShowDriver;
import org.dandelion.radiot.accepttest.testables.TestingLiveShowApp;
import org.dandelion.radiot.live.LiveShowApp;
import org.dandelion.radiot.live.core.LiveShowPlayer;
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
        app = new TestingLiveShowApp();
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
        app.assertIsPlaying();
	}
	
	public void testStopPlaybackWhenPressingStop() throws Exception {
        driver.startTranslation();
        driver.assertShowsTranslation();

        driver.stopTranslation();
        driver.assertShowsStopped();
        app.assertIsStopped();
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
        configureForConnectError();

        driver.startTranslation();
        driver.assertIsWaiting();

		// Switch back to existing URL
        app.setAudioUrl(TEST_LIVE_URL);
        driver.assertShowsTranslation();
	}

	
	public void testStopWaiting() throws Exception {
        configureForConnectError();

        driver.startTranslation();
        driver.assertIsWaiting();

        driver.stopTranslation();
        driver.assertShowsStopped();
	}
	
	private void configureForConnectError() {
		// Try to beConnecting to non-existent url to simulate
        app.setAudioUrl("http://non-existent");
		// And set the wait timeout to a small value
		LiveShowPlayer.setWaitTimeoutSeconds(1);
	}
}
