package org.dandelion.radiot.unittest.live;

import junit.framework.TestCase;
import org.dandelion.radiot.helpers.MockMediaPlayer;
import org.dandelion.radiot.live.core.LiveShowState;

public class BasicLiveShowStateTestCase extends TestCase {
    protected LiveShowState switchedState;
    protected LiveShowState currentState;
    protected LiveShowState.ILiveShowService service;
    protected MockMediaPlayer player;
    protected boolean serviceIsForeground;
    protected boolean timeoutScheduled;
    protected boolean wifiLocked;

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
	}

    protected void assertSwitchedToState(Class<?> stateClass) {
        assertStateClass(switchedState, stateClass);
    }

    protected void assertStateClass(LiveShowState newState, Class<?> stateClass) {
        if (null == newState)
            fail("Not switched to any state");
        assertEquals(stateClass, newState.getClass());
    }
}
