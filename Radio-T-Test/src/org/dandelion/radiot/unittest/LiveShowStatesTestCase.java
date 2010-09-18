package org.dandelion.radiot.unittest;

import java.util.Timer;
import java.util.TimerTask;

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
		assertCurrentState(LiveShowState.Connecting.class);
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
	
	public void testSwitchingToWaitingStateOnPlaybackError() throws Exception {
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
	
	public void testPreparingForPlayingWhenEntersConnectingState() throws Exception {
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
	
	public void testWaitsForTimeoutAndGoesToConnectingState() throws Exception {
		TestTimer timer = new TestTimer();
		currentState = new LiveShowState.Waiting(player, service, timer);
		currentState.enter();
		timer.timeoutElapsed();
		assertCurrentState(LiveShowState.Connecting.class);
		
	}
	
	public void testResetsPlayerWhenEntersWaitingState() throws Exception {
		currentState = new LiveShowState.Waiting(player, service, new TestTimer());
		player.prepareAsync();
		currentState.enter();
		player.assertIsReset();
	}
	
	public void testCancelsTheTimerWhenStoppingPlayback() throws Exception {
		TestTimer timer = new TestTimer();
		currentState = new LiveShowState.Waiting(player, service, timer);
		currentState.enter();
		currentState.stopPlayback();
		assertTrue(timer.isCancelled());
		assertCurrentState(LiveShowState.Stopping.class);
	}
	
	public void testEnteringToStoppingStateResetsPlayerAndGoesIdle() throws Exception {
		player.bePrepared();
		
		currentState = new LiveShowState.Stopping(player, service);
		currentState.enter();
		
		player.assertIsReset();
		assertCurrentState(LiveShowState.Idle.class);
	}
	
	private void assertCurrentState(Class<?> stateClass) {
		if (null == switchedState) 
			fail("Not switched to any state");
		assertEquals(stateClass, switchedState.getClass());
	}
	
	class TestTimer extends Timer {
		private TimerTask task;
		private boolean cancelled = false;

		@Override
		public void schedule(TimerTask task, long delay) {
			this.task = task;
		}

		public boolean isCancelled() {
			return cancelled;
		}
		
		@Override
		public void cancel() {
			cancelled = true;
		}

		public void timeoutElapsed() {
			task.run();
		}
	}
}
