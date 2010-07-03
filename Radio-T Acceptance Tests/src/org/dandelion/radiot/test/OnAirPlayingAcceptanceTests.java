package org.dandelion.radiot.test;

import org.dandelion.radiot.IPodcastPlayer;
import org.dandelion.radiot.OnAirActivity;

import com.jayway.android.robotium.solo.Solo;

import android.test.ActivityInstrumentationTestCase2;

public class OnAirPlayingAcceptanceTests extends
		ActivityInstrumentationTestCase2<OnAirActivity> implements IPodcastPlayer {

	private Solo solo;
	private String urlBeingPlayed;

	public OnAirPlayingAcceptanceTests() {
		super("org.dandelion.radiot", OnAirActivity.class);
	}
	
	@Override
	protected void setUp() throws Exception {
		super.setUp();
		OnAirActivity activity = getActivity();
		activity.setPodcastPlayer(this);
		solo = new Solo(getInstrumentation(), activity);
	}
	
	public void testStartPlayingAudio() throws Exception {
		solo.clickOnButton("Слушать");
		assertStartedPlaying(OnAirActivity.LIVE_SHOW_URL);
	}

	private void assertStartedPlaying(String url) {
		assertEquals(url, urlBeingPlayed);
	}

	@Override
	public void startPlaying(String url) {
		urlBeingPlayed = url;
	}

}
