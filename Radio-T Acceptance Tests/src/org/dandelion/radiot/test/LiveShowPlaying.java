package org.dandelion.radiot.test;

import org.dandelion.radiot.LiveShowScreen;
import org.dandelion.radiot.test.helpers.ApplicationDriver;
import org.dandelion.radiot.test.helpers.BasicAcceptanceTestCase;
import org.dandelion.radiot.test.helpers.FakePodcastPlayer;

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
