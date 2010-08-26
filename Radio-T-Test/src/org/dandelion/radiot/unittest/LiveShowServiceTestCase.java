package org.dandelion.radiot.unittest;

import java.io.IOException;

import junit.framework.Assert;

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
	
	public LiveShowServiceTestCase() {
		super(LiveShowService.class);
	}

	@Override
	protected void setUp() throws Exception {
		super.setUp();
		player = new TestMediaPlayer();
		bindService(new Intent());
		service = getService();
		service.setMediaPlayer(player);
	}

	@Override
	protected void tearDown() throws Exception {
		player.release();
		super.tearDown();
	}

	public void testStartPlaybackStartsPlaying() throws Exception {
		service.startPlayback();
		assertTrue(player.isPlaying());
	}

	public void testSendsBrodcastNotificationWhenStartsPlaying()
			throws Exception {
		(new BroadcastCatcher(getContext(),
				LiveShowService.PLAYBACK_STATE_CHANGED) {
			@Override
			public void run() {
				service.startPlayback();
			}
		}).assertCaught();
	}

	public void testSubsequentStartPlaybacksDoNotCrash() throws Exception {
		try {
			service.startPlayback();
			service.startPlayback();
		} catch (Exception e) {
			fail(e.getMessage());
		}
	}

	public void testStopPlaybackStopsPlaying() throws Exception {
		service.startPlayback();
		service.stopPlayback();
		assertFalse(player.isPlaying());
	}

	public void testSendsBrodcastNotificationWhenStopsPlaying()
			throws Exception {
		service.startPlayback();
		(new BroadcastCatcher(getContext(),
				LiveShowService.PLAYBACK_STATE_CHANGED) {
			@Override
			public void run() {
				service.stopPlayback();
			}
		}).assertCaught();
	}
}

abstract class BroadcastCatcher extends BroadcastReceiver {
	private boolean broadcastReceived = false;
	private IntentFilter filter;
	private Context context;

	public BroadcastCatcher(Context context, String action) {
		filter = new IntentFilter(action);
		this.context = context;
	}

	public void onReceive(Context context, Intent intent) {
		broadcastReceived = true;
	}

	public abstract void run();

	public void assertCaught() {
		context.registerReceiver(this, filter);
		try {
			run();
			Thread.yield();
			Assert.assertTrue("No broadcast received", broadcastReceived);
		} finally {
			context.unregisterReceiver(this);
		}
	}
}

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
		onPrepared.onPrepared(this);
	}
}
