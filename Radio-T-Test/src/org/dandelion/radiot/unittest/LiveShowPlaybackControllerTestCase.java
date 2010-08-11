package org.dandelion.radiot.unittest;

import java.io.IOException;

import junit.framework.Assert;
import junit.framework.TestCase;

import org.dandelion.radiot.live.LiveShowPlaybackController;

import android.media.MediaPlayer;

public class LiveShowPlaybackControllerTestCase extends TestCase {
	
	private MockMediaPlayer mockPlayer;
	private LiveShowPlaybackController controller;

	@Override
	protected void setUp() throws Exception {
		super.setUp();
		mockPlayer = new MockMediaPlayer();
		controller = new LiveShowPlaybackController(mockPlayer);
	}
	
	public void testStartsPlaying() throws Exception {
		controller.start("url-to-play");
		mockPlayer.bePrepared();
		
		mockPlayer.assertIsPlaying("url-to-play");
	}
	
	public void testStopsPLaying() throws Exception {
		controller.start("url-to-play");
		mockPlayer.bePrepared();

		controller.stop();
		mockPlayer.assertIsReset();
	}
}

class MockMediaPlayer extends MediaPlayer {
	private String dataSource;
	private OnPreparedListener onPreparedListener;
	private boolean isStarted = false;
	private boolean isReset;

	@Override
	public void setDataSource(String path) throws IOException,
			IllegalArgumentException, IllegalStateException {
		dataSource = path;
	}
	
	public void assertIsReset() {
		Assert.assertTrue(isReset);
	}

	public void assertIsPlaying(String expectedUrl) {
		Assert.assertTrue(isStarted);
		Assert.assertEquals(expectedUrl, dataSource);
	}

	public void bePrepared() {
		onPreparedListener.onPrepared(this);
	}

	@Override
	public void setOnPreparedListener(OnPreparedListener listener) {
		onPreparedListener = listener;
	}
	
	@Override
	public void prepareAsync() throws IllegalStateException {
	}
	
	@Override
	public void start() throws IllegalStateException {
		isStarted = true;
	}
	
	@Override
	public void reset() {
		isReset = true;
	}
}
