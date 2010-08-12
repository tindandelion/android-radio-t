package org.dandelion.radiot.helpers;

import java.io.IOException;

import junit.framework.Assert;
import android.media.MediaPlayer;
import android.util.Log;

public class TestMediaPlayer extends MediaPlayer {

	private String dataSource;
	private OnPreparedListener onPreparedListener;
	
	@Override
	public void setOnPreparedListener(OnPreparedListener listener) {
		super.setOnPreparedListener(listener);
		onPreparedListener = listener;
	}

	@Override
	public void setDataSource(String path) throws IOException,
			IllegalArgumentException, IllegalStateException {

		try {
			super.setDataSource(path);
		} catch (Exception e) {
			Assert.fail("Error while setting data source");
			Log.e("RadioT", "Error", e);
		}
		dataSource = path;
	}

	@Override
	public void prepareAsync() throws IllegalStateException {
		try {
			super.prepare();
		} catch (IOException e) {
			Assert.fail("prepareAsync failed");
		}
		onPreparedListener.onPrepared(this);
	}

	public void assertIsPlaying(String path) {
		Assert.assertTrue(isPlaying());
		Assert.assertEquals(path, dataSource);
	}

	public void assertStopped() {
		Assert.assertFalse(isPlaying());
	}

}
