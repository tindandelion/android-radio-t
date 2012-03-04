package org.dandelion.radiot.unittest.live;

import junit.framework.TestCase;
import org.dandelion.radiot.helpers.MockMediaPlayer;
import org.dandelion.radiot.live.core.AudioStream;
import org.dandelion.radiot.live.core.PlaybackContext;
import org.dandelion.radiot.live.core.states.PlaybackState;

public class PlaybackStateTestCase extends TestCase {
    protected PlaybackState switchedState;
    protected PlaybackState.ILiveShowService service;
    protected MockMediaPlayer player;
    protected PlaybackContext context;
    protected boolean serviceIsForeground;
    protected boolean timeoutScheduled;
    protected boolean wifiLocked;

    @Override
	protected void setUp() throws Exception {
		super.setUp();
		service = new PlaybackState.ILiveShowService() {
			public void switchToNewState(PlaybackState newState) {
				switchedState = newState;
			}

			public void goForeground(int stringId) {
				serviceIsForeground = true;
			}

			public void goBackground() {
				serviceIsForeground = false;
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
        AudioStream stream = new AudioStream(player);
        context = new PlaybackContext(service, stream, null);
	}

}
