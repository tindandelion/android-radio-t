package org.dandelion.radiot.unittest;

import java.io.IOException;

import junit.framework.Assert;

import org.dandelion.radiot.RadiotApplication;
import org.dandelion.radiot.live.LiveShowService;

import org.dandelion.radiot.live.LiveShowState.StateNames;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.media.MediaPlayer;
import android.test.ServiceTestCase;

public class LiveShowServiceTestCase extends ServiceTestCase<LiveShowService> {

	private LiveShowService service;
	private TestMediaPlayer player;

	public LiveShowServiceTestCase() {
		super(LiveShowService.class);
	}

	@Override
	protected void setUp() throws Exception {
		super.setUp();
		player = new TestMediaPlayer();
		setApplication(new RadiotApplication() {
			@Override
			public MediaPlayer getMediaPlayer() {
				return player;
			}
		});
		bindService(new Intent());
		service = getService();
	}

	@Override
	protected void tearDown() throws Exception {
		player.release();
		super.tearDown();
	}

	public void testStartPlaybackSwitchesToWaitingState() throws Exception {
		service.startPlayback();
		assertEquals(StateNames.Waiting, service.getState());
	}

	public void testSwitchesToPlayingStateWhenPrepared() throws Exception {
		service.startPlayback();
		player.bePrepared();
		assertTrue(player.isPlaying());
		assertEquals(StateNames.Playing, service.getState());
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
		player.bePrepared();
		service.stopPlayback();
		assertFalse(player.isPlaying());
		assertEquals(StateNames.Idle, service.getState());
	}

	public void testSendsBrodcastNotificationWhenStopsPlaying()
			throws Exception {
		service.startPlayback();
		player.bePrepared();
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
	}

	public void bePrepared() {
		if (null == onPrepared)
			return;
		
		onPrepared.onPrepared(this);
	}
}
