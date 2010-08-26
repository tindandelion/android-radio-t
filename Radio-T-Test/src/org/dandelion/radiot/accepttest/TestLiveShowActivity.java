package org.dandelion.radiot.accepttest;

import org.dandelion.radiot.live.LiveShowActivity;

public class TestLiveShowActivity extends LiveShowActivity {
	
	private boolean playbackStateChanged = false;

	@Override
	protected void playbackStateChanged() {
		playbackStateChanged = true;
	}

	public boolean isPlaybackStateChanged() {
		return playbackStateChanged;
	}
}
