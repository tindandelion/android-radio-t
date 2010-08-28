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
		};
		player = new MockMediaPlayer();
	}

	public void testSwitchingFromIdleToWaitingState() throws Exception {
		currentState = new LiveShowState.Idle(player, service);
		currentState.startPlayback();
		assertSwitchedToState(LiveShowState.Waiting.class);
	}
	
	public void testResetsPlayerWhenEntersIdleState() throws Exception {
		currentState = new LiveShowState.Idle(player, service);
		currentState.enter();
		player.assertIsReset();
	}
	
	public void testGoesBackgroundWhenEntersIdleState() throws Exception {
		currentState = new LiveShowState.Idle(player, service);
		
		serviceIsForeground = true;
		currentState.enter();
		
		assertFalse(serviceIsForeground);
	}
	
	public void testStartsPlaybackWhenEntersPlayingState() throws Exception {
		currentState = new LiveShowState.Playing(player, service);
		
		player.bePrepared();
		currentState.enter();
		
		player.assertIsPlaying(null);
	}
	
	public void testGoesToIdleStateWhenStopsPlayback() throws Exception {
		currentState = new LiveShowState.Playing(player, service);
		currentState.stopPlayback();
		assertSwitchedToState(LiveShowState.Idle.class);
	}

	private void assertSwitchedToState(Class<?> stateClass) {
		if (null == switchedState) 
			fail("Not switched to any state");
		assertEquals(stateClass, switchedState.getClass());
	}
	
	public void testGoesForegroundWhenEntersPlayingState() throws Exception {
		currentState = new LiveShowState.Playing(player, service);
		
		player.bePrepared();
		currentState.enter();
		
		assertTrue(serviceIsForeground);
	}
	
	public void testSwitchingToWaitingStateOnPlaybackError() throws Exception {
		currentState = new LiveShowState.Playing(player, service);
		
		player.bePrepared();
		currentState.enter();
		player.signalError();
		
		assertSwitchedToState(LiveShowState.Waiting.class);
		
	}
	
	public void testResettingPlayerOnPlaybackError() throws Exception {
		currentState = new LiveShowState.Playing(player, service);
		
		player.bePrepared();
		currentState.enter();
		player.signalError();
		
		player.assertIsReset();
		
	}
	
	public void testPreparingForPlayingWhenEntersWaitingState() throws Exception {
		currentState = new LiveShowState.Waiting(player, service, "");
		currentState.enter();
		player.assertIsPreparing();
	}
	
	public void testGoesForegroundWhenEntersWaitingState() throws Exception {
		currentState = new LiveShowState.Waiting(player, service, "");
		currentState.enter();
		assertTrue(serviceIsForeground);
	}
	
	public void testSwitchingToPlayingStateWhenPrepared() throws Exception {
		currentState = new LiveShowState.Waiting(player, service, "");
		currentState.enter();
		player.bePrepared();
		assertSwitchedToState(LiveShowState.Playing.class);
	}
	
	public void testGoesToIdleStateOnErrorWhilePreparing() throws Exception {
		currentState = new LiveShowState.Waiting(player, service, "");
		currentState.enter();
		player.signalError();
		assertSwitchedToState(LiveShowState.Idle.class);
	}
}
