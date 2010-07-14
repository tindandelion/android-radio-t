package org.dandelion.radiot.test;

import org.dandelion.radiot.LiveShowActivity;
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
		LiveShowActivity activity = appDriver.visitLiveShowPage();
		activity.setPodcastPlayer(player);
	}
	
	public void testStartPlayingAudio() throws Exception {
		appDriver.clickOnButton("Слушать");
		player.assertIsPlaying(LiveShowActivity.LIVE_SHOW_URL);
	}
}
