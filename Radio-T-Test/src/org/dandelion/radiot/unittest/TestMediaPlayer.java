package org.dandelion.radiot.unittest;

import java.io.IOException;

import android.media.MediaPlayer;
import android.media.MediaPlayer.OnPreparedListener;

class TestMediaPlayer extends MediaPlayer {
	private OnPreparedListener onPrepared;

	@Override
	public void setOnPreparedListener(OnPreparedListener listener) {
		super.setOnPreparedListener(listener);
		onPrepared = listener;
	}

	@Override
	public void prepareAsync() throws IllegalStateException {
		try {
			prepare();
		} catch (IOException e) {
			throw new RuntimeException(e);
		}
	}

	public void bePrepared() {
		if (null == onPrepared)
			return;
		
		onPrepared.onPrepared(this);
	}
}