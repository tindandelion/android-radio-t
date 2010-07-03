package org.dandelion.radiot.test;

import org.dandelion.radiot.OnAirActivity;

import com.jayway.android.robotium.solo.Solo;

import android.test.ActivityInstrumentationTestCase2;

public class OnAirPlayingAcceptanceTests extends
		ActivityInstrumentationTestCase2<OnAirActivity> {

	private Solo solo;

	public OnAirPlayingAcceptanceTests() {
		super("org.dandelion.radiot", OnAirActivity.class);
	}
	
	@Override
	protected void setUp() throws Exception {
		super.setUp();
		solo = new Solo(getInstrumentation(), getActivity());
	}
	
	public void testStartPlayingAudio() throws Exception {
		solo.clickOnButton("Слушать");
		assertStartedPlaying(OnAirActivity.LIVE_SHOW_URL);
	}

	private void assertStartedPlaying(String url) {
		fail("Not yet implemented");
	}

}
