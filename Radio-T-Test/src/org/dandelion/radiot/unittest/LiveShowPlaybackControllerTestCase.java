package org.dandelion.radiot.unittest;

import java.io.IOException;

import junit.framework.Assert;
import junit.framework.TestCase;

import org.dandelion.radiot.live.LiveShowPlaybackController;

import android.media.MediaPlayer;

public class LiveShowPlaybackControllerTestCase extends TestCase {
	
	private MockMediaPlayer mockPlayer;
	private LiveShowPlaybackController controller;
	private TestPlaybackView view;

	@Override
	protected void setUp() throws Exception {
		super.setUp();
		mockPlayer = new MockMediaPlayer();
		view = new TestPlaybackView();
		controller = new LiveShowPlaybackController(mockPlayer);
		controller.attach(view);
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
	
	public void testTogglePlaying() throws Exception {
		controller.start("url-to-play");
		mockPlayer.bePrepared();
		
		controller.togglePlaying(false);
		mockPlayer.assertIsReset();
		
		controller.togglePlaying(true);
		mockPlayer.bePrepared();
		mockPlayer.assertIsPlaying("url-to-play");
	}
	
	public void testDisablesControlsWhilePreparing() throws Exception {
		controller.start("");
		view.assertControlsEnabled(false);
		
		mockPlayer.bePrepared();
		view.assertControlsEnabled(true);
	}
	
	public void testCallsBackToViewOnStartAndStop() throws Exception {
		controller.start("");
		view.assertIsPlaying(false);
		mockPlayer.bePrepared();
		
		view.assertIsPlaying(true);
		
		controller.stop();
		view.assertIsPlaying(false);
		
	}
}

class TestPlaybackView implements LiveShowPlaybackController.ILivePlaybackView {

	private boolean controlsEnabled;
	private boolean isPlaying;

	public void assertControlsEnabled(boolean expected) {
		Assert.assertEquals(expected, controlsEnabled);
	}

	public void assertIsPlaying(boolean expected) {
		Assert.assertEquals(expected, isPlaying);
	}

	public void enableControls(boolean enabled) {
		controlsEnabled = enabled;
	}

	public void setPlaying(boolean playing) {
		isPlaying = playing;
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
		isReset = false;
	}
	
	@Override
	public void reset() {
		isReset = true;
		isStarted = false;
		dataSource = null;
	}
}
