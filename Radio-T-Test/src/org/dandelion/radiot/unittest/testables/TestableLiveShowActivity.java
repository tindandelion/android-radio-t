package org.dandelion.radiot.unittest.testables;

import org.dandelion.radiot.live.LiveShowActivity;

public class TestableLiveShowActivity extends LiveShowActivity {
	private boolean playbackStateChanged = false;

	@Override
	protected void receivePlaybackStateChangedBroadcast() {
		playbackStateChanged = true;
	}

	public boolean playbackStateChanged() {
		return playbackStateChanged;
	}

	public boolean isServiceConnected() {
		return service != null;
	}

}
