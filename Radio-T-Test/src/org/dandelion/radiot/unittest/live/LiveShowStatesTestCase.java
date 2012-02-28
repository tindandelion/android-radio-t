package org.dandelion.radiot.unittest.live;

import org.dandelion.radiot.live.core.LiveShowState;

public class LiveShowStatesTestCase extends BasicLiveShowStateTestCase {
    // ------ Idle state tests

	public void testSwitchingFromIdleToWaitingState() throws Exception {
		currentState = new LiveShowState.Idle(player, service);
		currentState.startPlayback();
		assertSwitchedToState(LiveShowState.Connecting.class);
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
		assertSwitchedToState(LiveShowState.Stopping.class);
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

		assertSwitchedToState(LiveShowState.Connecting.class);

	}

	public void testResettingPlayerOnPlaybackError() throws Exception {
		currentState = new LiveShowState.Playing(player, service);

		player.bePrepared();
		currentState.enter();
		player.signalError();

		player.assertIsReset();

	}
	
	public void testLocksWifiWhenEntersState() throws Exception {
		currentState = new LiveShowState.Playing(player, service);
		
		player.bePrepared();
		currentState.enter();
		
		assertTrue(wifiLocked);
	}
	
	public void testReleasesWifiWhenLeaveState() throws Exception {
		currentState = new LiveShowState.Playing(player, service);
		wifiLocked = true;
		currentState.leave();
		assertFalse(wifiLocked);
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
		assertSwitchedToState(LiveShowState.Playing.class);
	}

	public void testGoesToWaitingStateOnErrorWhilePreparing() throws Exception {
		currentState = new LiveShowState.Connecting(player, service);
		currentState.enter();
		player.signalError();
		assertSwitchedToState(LiveShowState.Waiting.class);
	}


	// ------ Stopping state tests

	public void testEnteringToStoppingStateResetsPlayerAndGoesIdle()
			throws Exception {
		player.bePrepared();

		currentState = new LiveShowState.Stopping(player, service);
		currentState.enter();

		player.assertIsReset();
		assertSwitchedToState(LiveShowState.Idle.class);
	}
}
