package org.dandelion.radiot.unittest;

import junit.framework.TestCase;

import org.dandelion.radiot.helpers.MockMediaPlayer;
import org.dandelion.radiot.live.LiveShowState;
import org.dandelion.radiot.live.LiveShowState.ILiveShowService;

public class LiveShowStatesTestCase extends TestCase {
	protected LiveShowState switchedState;
	private LiveShowState currentState;

	private ILiveShowService service;

	private MockMediaPlayer player;
	private boolean serviceIsForeground;
	protected Runnable scheduledAction;

	@Override
	protected void setUp() throws Exception {
		super.setUp();
		service = new LiveShowState.ILiveShowService() {
			public void switchToNewState(LiveShowState newState) {
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

			public Object scheduleAction(Runnable action, int timeoutSeconds) {
				scheduledAction = action;
				return null;
			}

			public void cancelScheduledAction(Object token) {
				scheduledAction = null;
			}

		};
		player = new MockMediaPlayer();
	}

	// ------ Idle state tests

	public void testSwitchingFromIdleToWaitingState() throws Exception {
		currentState = new LiveShowState.Idle(player, service);
		currentState.startPlayback();
		assertCurrentState(LiveShowState.Connecting.class);
	}

	public void testGoesBackgroundWhenEntersIdleState() throws Exception {
		currentState = new LiveShowState.Idle(player, service);

		serviceIsForeground = true;
		currentState.enter();

		assertFalse(serviceIsForeground);
	}

	// ------ Playing state tests

	public void testStartsPlaybackWhenEntersPlayingState() throws Exception {
		currentState = new LiveShowState.Playing(player, service);

		player.bePrepared();
		currentState.enter();

		player.assertIsPlaying(null);
	}

	public void testGoesToStoppingStateWhenStopsPlayback() throws Exception {
		currentState = new LiveShowState.Playing(player, service);
		currentState.stopPlayback();
		assertCurrentState(LiveShowState.Stopping.class);
	}

	public void testGoesForegroundWhenEntersPlayingState() throws Exception {
		currentState = new LiveShowState.Playing(player, service);

		player.bePrepared();
		currentState.enter();

		assertTrue(serviceIsForeground);
	}

	public void testSwitchingToConnectingStateOnPlaybackError()
			throws Exception {
		currentState = new LiveShowState.Playing(player, service);

		player.bePrepared();
		currentState.enter();
		player.signalError();

		assertCurrentState(LiveShowState.Connecting.class);

	}

	public void testResettingPlayerOnPlaybackError() throws Exception {
		currentState = new LiveShowState.Playing(player, service);

		player.bePrepared();
		currentState.enter();
		player.signalError();

		player.assertIsReset();

	}

	// ------ Connecting state tests

	public void testPreparingForPlayingWhenEntersConnectingState()
			throws Exception {
		currentState = new LiveShowState.Connecting(player, service);
		currentState.enter();
		player.assertIsPreparing();
	}

	public void testGoesForegroundWhenEntersConnectingState() throws Exception {
		currentState = new LiveShowState.Connecting(player, service);
		currentState.enter();
		assertTrue(serviceIsForeground);
	}

	public void testSwitchingToPlayingStateWhenPrepared() throws Exception {
		currentState = new LiveShowState.Connecting(player, service);
		currentState.enter();
		player.bePrepared();
		assertCurrentState(LiveShowState.Playing.class);
	}

	public void testGoesToWaitingStateOnErrorWhilePreparing() throws Exception {
		currentState = new LiveShowState.Connecting(player, service);
		currentState.enter();
		player.signalError();
		assertCurrentState(LiveShowState.Waiting.class);
	}

	// ------ Waiting state tests

	public void testWaitsForTimeoutAndGoesToConnectingState() throws Exception {
		currentState = new LiveShowState.Waiting(player, service);
		currentState.enter();
		assertNotNull(scheduledAction);
		scheduledAction.run();
		assertCurrentState(LiveShowState.Connecting.class);

	}

	public void testResetsPlayerWhenEntersWaitingState() throws Exception {
		currentState = new LiveShowState.Waiting(player, service);
		player.prepareAsync();
		currentState.enter();
		player.assertIsReset();
	}

	public void testCancelsTheScheduledActionWhenStoppingPlayback()
			throws Exception {
		currentState = new LiveShowState.Waiting(player, service);
		currentState.enter();
		currentState.stopPlayback();
		assertNull(scheduledAction);
		assertCurrentState(LiveShowState.Stopping.class);
	}

	// ------ Stopping state tests

	public void testEnteringToStoppingStateResetsPlayerAndGoesIdle()
			throws Exception {
		player.bePrepared();

		currentState = new LiveShowState.Stopping(player, service);
		currentState.enter();

		player.assertIsReset();
		assertCurrentState(LiveShowState.Idle.class);
	}

	// ------ Helpers

	private void assertCurrentState(Class<?> stateClass) {
		if (null == switchedState)
			fail("Not switched to any state");
		assertEquals(stateClass, switchedState.getClass());
	}
}
