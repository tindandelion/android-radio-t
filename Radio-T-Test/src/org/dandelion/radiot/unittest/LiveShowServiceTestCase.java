package org.dandelion.radiot.unittest;

import junit.framework.Assert;

import org.dandelion.radiot.RadiotApplication;
import org.dandelion.radiot.helpers.MockMediaPlayer;
import org.dandelion.radiot.live.LiveShowService;

import android.app.Application;
import android.content.Intent;
import android.media.MediaPlayer;
import android.test.ServiceTestCase;

public class LiveShowServiceTestCase extends ServiceTestCase<LiveShowService> {

	private LiveShowService service;
	private MockMediaPlayer mockPlayer;
	private TestPlaybackView view;

	public LiveShowServiceTestCase() {
		super(LiveShowService.class);
	}
	
	@Override
	protected void setUp() throws Exception {
		super.setUp();
		setApplication(createTestApplication());
		bindService(new Intent());
		view = new TestPlaybackView();
		service = getService();
		service.attach(view);
	}
	
	@Override
	public void testServiceTestCaseSetUpProperly() throws Exception {
		// Stupid test - stub it out
	}
	
	public void testStartsPlaying() throws Exception {
		service.startPlaying("url-to-play");
		mockPlayer.bePrepared();

		mockPlayer.assertIsPlaying("url-to-play");
	}
	
	public void testStopsPLaying() throws Exception {
		service.startPlaying("url-to-play");
		mockPlayer.bePrepared();

		service.stopPlaying();
		mockPlayer.assertIsReset();
	}
	
	public void testTogglePlaying() throws Exception {
		service.startPlaying("url-to-play");
		mockPlayer.bePrepared();

		service.togglePlaying(false);
		mockPlayer.assertIsReset();

		service.togglePlaying(true);
		mockPlayer.bePrepared();
		mockPlayer.assertIsPlaying("url-to-play");
	}
	
	public void testDisablesControlsWhilePreparing() throws Exception {
		service.startPlaying("");
		view.assertControlsEnabled(false);

		mockPlayer.bePrepared();
		view.assertControlsEnabled(true);
	}
	
	public void testCallsBackToViewOnStartAndStop() throws Exception {
		service.startPlaying("");
		view.assertIsPlaying(false);
		mockPlayer.bePrepared();

		view.assertIsPlaying(true);

		service.stopPlaying();
		view.assertIsPlaying(false);
	}
	
	public void testUpdatesViewWhenAttached() throws Exception {
		TestPlaybackView newView = attachNewView();
		newView.assertState(true, false);

		service.startPlaying("");
		newView = attachNewView();
		newView.assertState(false, false);

		mockPlayer.bePrepared();
		newView = attachNewView();
		newView.assertState(true, true);

		service.stopPlaying();
		newView = attachNewView();
		newView.assertState(true, false);
	}

	public void testDontStartPlayingIfAlreadyDoes() throws Exception {
		service.startPlaying("");
		service.startPlaying("");

		mockPlayer.bePrepared();
		service.startPlaying("");
	}

	public void testHandleErrorsWhileConnecting() throws Exception {
		mockPlayer.throwsConnectionError();
		service.startPlaying("");
		view.assertShowsError();
		view.assertState(true, false);
	}

	public void testHandleErrorsWhilePreparing() throws Exception {
		service.startPlaying("");
		mockPlayer.prepareError();

		view.assertShowsError();
		view.assertState(true, false);
		mockPlayer.assertIsReset();
	}

	protected TestPlaybackView attachNewView() {
		TestPlaybackView newView = new TestPlaybackView();
		service.attach(newView);
		return newView;
	}


	private Application createTestApplication() {
		mockPlayer = new MockMediaPlayer();
		return new RadiotApplication() {
			@Override
			public MediaPlayer getMediaPlayer() {
				return mockPlayer;
			}
		};
	}
}

class TestPlaybackView implements LiveShowService.ILivePlaybackView {

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

