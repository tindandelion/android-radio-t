package org.dandelion.radiot.accepttest;

import org.dandelion.radiot.live.core.states.PlaybackState;
import org.dandelion.radiot.live.ui.LiveShowActivity;

import android.test.ActivityInstrumentationTestCase2;

import com.jayway.android.robotium.solo.Solo;

public class LiveShowPlayback extends
		ActivityInstrumentationTestCase2<LiveShowActivity> {

	private static final String TEST_LIVE_URL = "http://icecast.bigrradio.com/80s90s";
	private LiveShowActivity activity;
	private Solo solo;

	public LiveShowPlayback() {
		super("org.dandelion.radiot", LiveShowActivity.class);
	}

	@Override
	protected void setUp() throws Exception {
		super.setUp();
		PlaybackState.setLiveShowUrl(TEST_LIVE_URL);
		activity = getActivity();
		solo = new Solo(getInstrumentation(), activity);
	}
	
	@Override
	protected void tearDown() throws Exception {
		activity.getService().stopPlayback();
		super.tearDown();
	}
	
	public void testStartPlayback() throws Exception {
		solo.clickOnButton("Подключиться");
		assertTrue(solo.waitForText("Трансляция"));
	}
	
	public void testStopPlaybackWhenPressingStop() throws Exception {
		solo.clickOnButton("Подключиться");
		assertTrue(solo.waitForText("Трансляция"));
		solo.clickOnButton("Остановить");
		assertTrue(solo.waitForText("Остановлено"));
	}
	
	public void testRestartPlaybackAfterExplicitStop() throws Exception {
		solo.clickOnButton("Подключиться");
		assertTrue(solo.waitForText("Трансляция"));
		solo.clickOnButton("Остановить");
		solo.clickOnButton("Подключиться");
		assertTrue(solo.waitForText("Трансляция"));
	}
	
	public void testTryToReconnectContinuouslyInWaitingMode() throws Exception {
		configureForConnectError();
		
		solo.clickOnButton("Подключиться");
		assertTrue(solo.waitForText("Ожидание"));
		
		// Switch back to existing URL 
		PlaybackState.setLiveShowUrl(TEST_LIVE_URL);
		assertTrue(solo.waitForText("Трансляция"));
	}

	
	public void testStopWaiting() throws Exception {
		configureForConnectError();
		
		solo.clickOnButton("Подключиться");
		assertTrue(solo.waitForText("Ожидание"));
		solo.clickOnButton("Остановить");
		
		assertTrue(solo.waitForText("Остановлено"));
	}
	
	private void configureForConnectError() {
		// Try to connect to non-existent url to simulate 
		PlaybackState.setLiveShowUrl("http://non-existent");
		// And set the wait timeout to a small value
		PlaybackState.setWaitTimeoutSeconds(1);
	}
}
