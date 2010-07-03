package org.dandelion.radiot.test;

import org.dandelion.radiot.IPodcastPlayer;
import org.dandelion.radiot.LiveShowActivity;

import com.jayway.android.robotium.solo.Solo;

import android.net.Uri;
import android.test.ActivityInstrumentationTestCase2;

public class LivePlayingAcceptanceTests extends
		ActivityInstrumentationTestCase2<LiveShowActivity> implements IPodcastPlayer {

	private Solo solo;
	private Uri urlBeingPlayed;

	public LivePlayingAcceptanceTests() {
		super("org.dandelion.radiot", LiveShowActivity.class);
	}
	
	@Override
	protected void setUp() throws Exception {
		super.setUp();
		LiveShowActivity activity = getActivity();
		activity.setPodcastPlayer(this);
		solo = new Solo(getInstrumentation(), activity);
	}
	
	public void testStartPlayingAudio() throws Exception {
		solo.clickOnButton("Слушать");
		assertStartedPlaying(LiveShowActivity.LIVE_SHOW_URL);
	}

	private void assertStartedPlaying(Uri url) {
		assertEquals(url, urlBeingPlayed);
	}

	@Override
	public void startPlaying(Uri url) {
		urlBeingPlayed = url;
	}

}
