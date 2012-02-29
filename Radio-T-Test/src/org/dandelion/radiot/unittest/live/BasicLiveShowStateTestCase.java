package org.dandelion.radiot.unittest.live;

import junit.framework.TestCase;
import org.dandelion.radiot.helpers.MockMediaPlayer;
import org.dandelion.radiot.live.core.PlaybackContext;
import org.dandelion.radiot.live.core.states.BasicState;

public class BasicLiveShowStateTestCase extends TestCase {
    protected BasicState switchedState;
    protected BasicState.ILiveShowService service;
    protected MockMediaPlayer player;
    protected PlaybackContext context;
    protected boolean serviceIsForeground;
    protected boolean timeoutScheduled;
    protected boolean wifiLocked;

    @Override
	protected void setUp() throws Exception {
		super.setUp();
		service = new BasicState.ILiveShowService() {
			public void switchToNewState(BasicState newState) {
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

			public void resetTimeout() {
				timeoutScheduled = false;
			}

			public void setTimeout(int waitTimeout, Runnable action) {
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
        context = new PlaybackContext(service, player);
	}

    protected void assertSwitchedToState(Class<?> stateClass) {
        assertStateClass(switchedState, stateClass);
    }

    protected void assertStateClass(BasicState newState, Class<?> stateClass) {
        if (null == newState)
            fail("Not switched to any state");
        assertEquals(stateClass, newState.getClass());
    }
}
