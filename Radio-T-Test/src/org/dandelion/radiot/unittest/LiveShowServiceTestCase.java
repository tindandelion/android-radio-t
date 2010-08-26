package org.dandelion.radiot.unittest;

import java.io.IOException;

import org.dandelion.radiot.live.LiveShowService;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.media.MediaPlayer;
import android.test.ServiceTestCase;

public class LiveShowServiceTestCase extends ServiceTestCase<LiveShowService> {

	private LiveShowService service;
	private MediaPlayer player;
	private boolean notificationReceived;

	public LiveShowServiceTestCase() {
		super(LiveShowService.class);
	}

	@Override
	protected void setUp() throws Exception {
		super.setUp();
		player = new SyncMediaPlayer();
		bindService(new Intent());
		service = getService();
		service.setMediaPlayer(player);
	}

	public void testStartPlaybackStartsPlaying() throws Exception {
		service.startPlayback();
		assertTrue(player.isPlaying());
	}

	public void testSendsBrodcastNotificationWhenStartsPlaying()
			throws Exception {
		notificationReceived = false;
		BroadcastReceiver onReceive = new BroadcastReceiver() {
			@Override
			public void onReceive(Context context, Intent intent) {
				if (intent.getAction().equals(
						LiveShowService.PLAYBACK_STATE_CHANGED)) {
					notificationReceived = true;
				}
			}
		};

		IntentFilter filter = new IntentFilter(
				LiveShowService.PLAYBACK_STATE_CHANGED);

		getContext().registerReceiver(onReceive, filter);

		service.startPlayback();
		assertTrue(notificationReceived);
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
