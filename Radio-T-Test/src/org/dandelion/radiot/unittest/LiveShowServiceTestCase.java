package org.dandelion.radiot.unittest;

import java.io.IOException;

import org.dandelion.radiot.RadiotApplication;
import org.dandelion.radiot.live.LiveShowService;

import android.content.Intent;
import android.media.MediaPlayer;
import android.media.MediaPlayer.OnPreparedListener;
import android.test.ServiceTestCase;

public class LiveShowServiceTestCase extends ServiceTestCase<LiveShowService> {

	private LiveShowService service;
	private MediaPlayer player;

	public LiveShowServiceTestCase() {
		super(LiveShowService.class);
	}
	
	@Override
	protected void setUp() throws Exception {
		super.setUp();
		player = new SyncMediaPlayer();
		setApplication(new RadiotApplication() {
			@Override
			public MediaPlayer getMediaPlayer() {
				return player;
			}
		});
		
		bindService(new Intent());
		service = getService();
	}
	
	public void testStartPlaybackStartsPlaying() throws Exception {
		service.startPlayback();
		assertTrue(player.isPlaying());
	}
}

class SyncMediaPlayer extends MediaPlayer {
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
		onPrepared.onPrepared(this);
	}
}
