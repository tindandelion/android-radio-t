package org.dandelion.radiot.unittest;

import java.io.IOException;

import junit.framework.Assert;
import junit.framework.TestCase;

import org.dandelion.radiot.live.LiveShowPlaybackController;
import org.dandelion.radiot.unittest.MockMediaPlayer.State;

import android.media.MediaPlayer;
import android.media.MediaPlayer.OnErrorListener;

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

	public void testUpdatesViewWhenAttached() throws Exception {
		TestPlaybackView newView = attachNewView();
		newView.assertState(true, false);

		controller.start("");
		newView = attachNewView();
		newView.assertState(false, false);

		mockPlayer.bePrepared();
		newView = attachNewView();
		newView.assertState(true, true);

		controller.stop();
		newView = attachNewView();
		newView.assertState(true, false);
	}

	public void testDontStartPlayingIfAlreadyDoes() throws Exception {
		controller.start("");
		controller.start("");

		mockPlayer.bePrepared();
		controller.start("");
	}

	public void testHandleErrorsWhileConnecting() throws Exception {
		mockPlayer.throwsConnectionError();
		controller.start("");
		view.assertShowsError();
		view.assertState(true, false);
	}

	public void testHandleErrorsWhilePreparing() throws Exception {
		controller.start("");
		mockPlayer.prepareError();

		view.assertShowsError();
		view.assertState(true, false);
		mockPlayer.assertIsReset();
	}

	protected TestPlaybackView attachNewView() {
		TestPlaybackView newView = new TestPlaybackView();
		controller.attach(newView);
		return newView;
	}
}

class TestPlaybackView implements LiveShowPlaybackController.ILivePlaybackView {

	private boolean controlsEnabled;
	private boolean isPlaying;
	private boolean showsError;

	public void assertControlsEnabled(boolean expected) {
		Assert.assertEquals(expected, controlsEnabled);
	}

	public void assertShowsError() {
		Assert.assertTrue(showsError);
	}

	public void assertState(boolean enabled, boolean playing) {
		assertControlsEnabled(enabled);
		assertIsPlaying(playing);
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

	public void showPlaybackError() {
		showsError = true;
	}

}

class MockMediaPlayer extends MediaPlayer {

	enum State {
		Idle, Preparing, Playing, Prepared
	}

	private String dataSource;
	private OnPreparedListener onPreparedListener;
	private State currentState = State.Idle;
	private IOException connectionError;
	private OnErrorListener onErrorListener;

	@Override
	public void setDataSource(String path) throws IOException,
			IllegalArgumentException, IllegalStateException {
		if (null != connectionError) {
			throw connectionError;
		}
		dataSource = path;
	}

	@Override
	public void setOnErrorListener(OnErrorListener listener) {
		onErrorListener = listener;
	}

	public void prepareError() {
		onErrorListener.onError(this, MEDIA_ERROR_UNKNOWN, 0);
	}

	public void throwsConnectionError() {
		connectionError = new IOException();
	}

	public void assertIsReset() {
		Assert.assertEquals(State.Idle, currentState);
	}

	public void assertIsPlaying(String expectedUrl) {
		Assert.assertEquals(State.Playing, currentState);
		Assert.assertEquals(expectedUrl, dataSource);
	}

	@Override
	public boolean isPlaying() {
		return State.Playing == currentState;
	}

	public void bePrepared() {
		currentState = State.Prepared;
		onPreparedListener.onPrepared(this);
	}

	@Override
	public void setOnPreparedListener(OnPreparedListener listener) {
		onPreparedListener = listener;
	}

	@Override
	public void prepareAsync() throws IllegalStateException {
		checkState(State.Idle);
		currentState = State.Preparing;
	}

	@Override
	public void start() throws IllegalStateException {
		checkState(State.Prepared);
		currentState = State.Playing;
	}

	private void checkState(State expected) {
		Assert.assertEquals(expected, currentState);
	}

	@Override
	public void reset() {
		currentState = State.Idle;
		dataSource = null;
	}
}
