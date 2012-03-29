package org.dandelion.radiot.accepttest;

import android.test.ActivityInstrumentationTestCase2;
import org.dandelion.radiot.accepttest.drivers.LiveShowDriver;
import org.dandelion.radiot.live.core.LiveShowPlayer;
import org.dandelion.radiot.live.ui.LiveShowActivity;

public class LiveShowPlaybackTest extends
		ActivityInstrumentationTestCase2<LiveShowActivity> {

	private static final String TEST_LIVE_URL = "http://icecast.bigrradio.com/80s90s";
	private LiveShowActivity activity;
	private LiveShowDriver driver;

    public LiveShowPlaybackTest() {
		super("org.dandelion.radiot", LiveShowActivity.class);
	}

	@Override
	protected void setUp() throws Exception {
		super.setUp();
		LiveShowPlayer.setLiveShowUrl(TEST_LIVE_URL);
		activity = getActivity();
		driver = new LiveShowDriver(getInstrumentation(), activity);
	}
	
	@Override
	protected void tearDown() throws Exception {
		activity.getService().reset();
		super.tearDown();
	}
	
	public void testStartPlayback() throws Exception {
        driver.startTranslation();
        driver.assertIsTranslating();
	}
	
	public void testStopPlaybackWhenPressingStop() throws Exception {
        driver.startTranslation();
        driver.assertIsTranslating();

        driver.stopTranslation();
        driver.assertIsStopped();
	}
	
	public void testRestartPlaybackAfterExplicitStop() throws Exception {
        driver.startTranslation();
        driver.assertIsTranslating();

        driver.stopTranslation();
        driver.assertIsStopped();

        driver.startTranslation();
        driver.assertIsTranslating();
	}
	
	public void testTryToReconnectContinuouslyInWaitingMode() throws Exception {
        configureForConnectError();

        driver.startTranslation();
        driver.assertIsWaiting();

		// Switch back to existing URL 
		LiveShowPlayer.setLiveShowUrl(TEST_LIVE_URL);
        driver.assertIsTranslating();
	}

	
	public void testStopWaiting() throws Exception {
        configureForConnectError();

        driver.startTranslation();
        driver.assertIsWaiting();

        driver.stopTranslation();
        driver.assertIsStopped();
	}
	
	private void configureForConnectError() {
		// Try to beConnecting to non-existent url to simulate
		LiveShowPlayer.setLiveShowUrl("http://non-existent");
		// And set the wait timeout to a small value
		LiveShowPlayer.setWaitTimeoutSeconds(1);
	}
}
