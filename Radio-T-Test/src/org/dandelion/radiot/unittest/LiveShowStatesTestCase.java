package org.dandelion.radiot.unittest;

import junit.framework.TestCase;

import org.dandelion.radiot.helpers.MockMediaPlayer;
import org.dandelion.radiot.live.states.*;
import org.dandelion.radiot.live.states.PlaybackService;

public class LiveShowStatesTestCase extends TestCase {
	protected PlaybackState switchedState;
	private PlaybackState currentState;

	private PlaybackService service;

	private MockMediaPlayer player;
	private boolean serviceIsForeground;
	protected boolean timeoutScheduled;
	private boolean wifiLocked;

	@Override
	protected void setUp() throws Exception {
		super.setUp();
		service = new PlaybackService() {
			public void switchToNewState(PlaybackState newState) {
				switchedState = newState;
			}

			public void goForeground(int stringId) {
				serviceIsForeground = true;
			}

			public void goBackground() {
				serviceIsForeground = false;
			}

			public void runAsynchronously(Runnable runnable) {
				runnable.run();
			}

			public void unscheduleTimeout() {
				timeoutScheduled = false;
			}

			public void scheduleTimeout(int waitTimeout) {
				timeoutScheduled = true;
			}
			
			public void lockWifi() {
				wifiLocked = true;
			}
			
			public void unlockWifi() { 
				wifiLocked = false;
			}

		};
		player = new MockMediaPlayer();
	}

	// ------ Idle state tests

	public void testSwitchingFromIdleToWaitingState() throws Exception {
		currentState = new Idle(player, service);
		currentState.startPlayback();
		assertCurrentState(Connecting.class);
	}

	public void testGoesBackgroundWhenEntersIdleState() throws Exception {
		currentState = new Idle(player, service);

		serviceIsForeground = true;
		currentState.enter();

		assertFalse(serviceIsForeground);
	}

	// ------ Playing state tests

	public void testStartsPlaybackWhenEntersPlayingState() throws Exception {
		currentState = new Playing(player, service);

		player.bePrepared();
		currentState.enter();

		player.assertIsPlaying(null);
	}

	public void testGoesToStoppingStateWhenStopsPlayback() throws Exception {
		currentState = new Playing(player, service);
		currentState.stopPlayback();
		assertCurrentState(Stopping.class);
	}

	public void testGoesForegroundWhenEntersPlayingState() throws Exception {
		currentState = new Playing(player, service);

		player.bePrepared();
		currentState.enter();

		assertTrue(serviceIsForeground);
	}

	public void testSwitchingToConnectingStateOnPlaybackError()
			throws Exception {
		currentState = new Playing(player, service);

		player.bePrepared();
		currentState.enter();
		player.signalError();

		assertCurrentState(Connecting.class);

	}

	public void testResettingPlayerOnPlaybackError() throws Exception {
		currentState = new Playing(player, service);

		player.bePrepared();
		currentState.enter();
		player.signalError();

		player.assertIsReset();

	}
	
	public void testLocksWifiWhenEntersState() throws Exception {
		currentState = new Playing(player, service);
		
		player.bePrepared();
		currentState.enter();
		
		assertTrue(wifiLocked);
	}
	
	public void testReleasesWifiWhenLeaveState() throws Exception {
		currentState = new Playing(player, service);
		wifiLocked = true;
		currentState.leave();
		assertFalse(wifiLocked);
	}

	// ------ Connecting state tests

	public void testPreparingForPlayingWhenEntersConnectingState()
			throws Exception {
		currentState = new Connecting(player, service);
		currentState.enter();
		player.assertIsPreparing();
	}

	public void testGoesForegroundWhenEntersConnectingState() throws Exception {
		currentState = new Connecting(player, service);
		currentState.enter();
		assertTrue(serviceIsForeground);
	}

	public void testSwitchingToPlayingStateWhenPrepared() throws Exception {
		currentState = new Connecting(player, service);
		currentState.enter();
		player.bePrepared();
		assertCurrentState(Playing.class);
	}

	public void testGoesToWaitingStateOnErrorWhilePreparing() throws Exception {
		currentState = new Connecting(player, service);
		currentState.enter();
		player.signalError();
		assertCurrentState(Waiting.class);
	}

	// ------ Waiting state tests

	public void testWaitsForTimeoutAndGoesToConnectingState() throws Exception {
		currentState = new Waiting(player, service);
		currentState.enter();
		assertTrue(timeoutScheduled);
		currentState.onTimeout();
		assertCurrentState(Connecting.class);

	}

	public void testResetsPlayerWhenEntersWaitingState() throws Exception {
		currentState = new Waiting(player, service);
		player.prepareAsync();
		currentState.enter();
		player.assertIsReset();
	}

	public void testCancelTimeoutWhenLeave()
			throws Exception {
		currentState = new Waiting(player, service);
		timeoutScheduled = true;
		currentState.leave();
		assertFalse(timeoutScheduled);
	}

	// ------ Stopping state tests

	public void testEnteringToStoppingStateResetsPlayerAndGoesIdle()
			throws Exception {
		player.bePrepared();

		currentState = new Stopping(player, service);
		currentState.enter();

		player.assertIsReset();
		assertCurrentState(Idle.class);
	}

	// ------ Helpers

	private void assertCurrentState(Class<?> stateClass) {
		if (null == switchedState)
			fail("Not switched to any state");
		assertEquals(stateClass, switchedState.getClass());
	}
}
