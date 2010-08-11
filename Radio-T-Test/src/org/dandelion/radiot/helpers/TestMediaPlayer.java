package org.dandelion.radiot.helpers;

import java.io.IOException;

import junit.framework.Assert;

import android.media.MediaPlayer;

public class TestMediaPlayer extends MediaPlayer {
	
	private String dataSource;

	@Override
	public void setDataSource(String path) throws IOException,
			IllegalArgumentException, IllegalStateException {
		super.setDataSource(path);
		dataSource = path;
	}
	
	@Override
	public void prepareAsync() throws IllegalStateException {
		try {
			super.prepare();
		} catch (IOException e) {
			Assert.fail("prepareAsync failed");
		}
	}

	public void assertIsPlaying(String path) {
		Assert.assertTrue(isPlaying());
		Assert.assertEquals(path, dataSource);
	}

	public void assertStopped() {
		Assert.assertFalse(isPlaying());
	}
	
}
