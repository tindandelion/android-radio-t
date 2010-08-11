package org.dandelion.radiot.accepttest;

import org.dandelion.radiot.helpers.ApplicationDriver;
import org.dandelion.radiot.helpers.BasicAcceptanceTestCase;
import org.dandelion.radiot.helpers.TestMediaPlayer;
import org.dandelion.radiot.live.LiveShowScreen;

public class LiveShowPlaying extends
		BasicAcceptanceTestCase {

	private TestMediaPlayer player;
	private ApplicationDriver appDriver;

	
	@Override
	protected void setUp() throws Exception {
		super.setUp();
		appDriver = createApplicationDriver();
		player = new TestMediaPlayer();
		getRadiotApplication().setMediaPlayer(player);
	}
	
	public void testStartPlayingAudio() throws Exception {
		appDriver.visitLiveShowPage();
		player.assertIsPlaying(LiveShowScreen.LIVE_SHOW_URL);
	}
	
	public void testStopPlayingOnExit() throws Exception {
		appDriver.visitLiveShowPage();
		appDriver.goBack();
		player.assertStopped();
	}
}
