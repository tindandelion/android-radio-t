package org.dandelion.radiot.accepttest;

import org.dandelion.radiot.LiveShowScreen;
import org.dandelion.radiot.helpers.ApplicationDriver;
import org.dandelion.radiot.helpers.BasicAcceptanceTestCase;
import org.dandelion.radiot.helpers.FakePodcastPlayer;

public class LiveShowPlaying extends
		BasicAcceptanceTestCase {

	private FakePodcastPlayer player;
	private ApplicationDriver appDriver;

	
	@Override
	protected void setUp() throws Exception {
		super.setUp();
		appDriver = createApplicationDriver();
		player = new FakePodcastPlayer();
		LiveShowScreen activity = appDriver.visitLiveShowPage();
		activity.setPodcastPlayer(player);
	}
	
	public void testStartPlayingAudio() throws Exception {
		appDriver.clickOnText("Слушать сейчас");
		player.assertIsPlaying(LiveShowScreen.LIVE_SHOW_URL);
	}
}
