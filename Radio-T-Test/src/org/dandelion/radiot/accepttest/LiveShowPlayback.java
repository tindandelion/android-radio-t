package org.dandelion.radiot.accepttest;

import org.dandelion.radiot.live.LiveShowActivity;

import android.test.ActivityInstrumentationTestCase2;

import com.jayway.android.robotium.solo.Solo;

public class LiveShowPlayback extends
		ActivityInstrumentationTestCase2<LiveShowActivity> {

	private LiveShowActivity activity;
	private Solo solo;

	public LiveShowPlayback() {
		super("org.dandelion.radiot", LiveShowActivity.class);
	}

	@Override
	protected void setUp() throws Exception {
		super.setUp();
		activity = getActivity();
		solo = new Solo(getInstrumentation(), activity);
	}
	
	@Override
	protected void tearDown() throws Exception {
		activity.getService().stopPlayback();
		super.tearDown();
	}
	
	public void testStartPlaybackWhenOpeningActivity() throws Exception {
		assertTrue(solo.waitForText("Трансляция"));
	}
	
	public void testStopPlaybackWhenPressingStop() throws Exception {
		assertTrue(solo.waitForText("Трансляция"));
		solo.clickOnButton("Остановить");
		assertTrue(solo.waitForText("Остановлено"));
	}
	
	public void testRestartPlaybackAfterExplicitStop() throws Exception {
		assertTrue(solo.waitForText("Трансляция"));
		solo.clickOnButton("Остановить");
		solo.clickOnButton("Подключиться");
		assertTrue(solo.waitForText("Трансляция"));
	}
}
