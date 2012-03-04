package org.dandelion.radiot.unittest.live;

import junit.framework.TestCase;
import org.dandelion.radiot.live.core.states.PlaybackState;

public class PlaybackStateTestCase extends TestCase {
    protected PlaybackState.ILiveShowService service;
    protected boolean serviceIsForeground;
    protected boolean wifiLocked;

    @Override
	protected void setUp() throws Exception {
		super.setUp();
		service = new PlaybackState.ILiveShowService() {
			public void goForeground(int stringId) {
				serviceIsForeground = true;
			}

			public void goBackground() {
				serviceIsForeground = false;
			}

			public void lockWifi() {
				wifiLocked = true;
			}

			public void unlockWifi() {
				wifiLocked = false;
			}

		};
	}

}
