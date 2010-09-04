package org.dandelion.radiot.live;

import org.dandelion.radiot.R;

import android.media.MediaPlayer;
import android.media.MediaPlayer.OnErrorListener;
import android.media.MediaPlayer.OnPreparedListener;

public abstract class LiveShowState {
//	private static final String LIVE_SHOW_URL = "http://stream3.radio-t.com:8181/stream";
	private static final String LIVE_SHOW_URL = "http://icecast.bigrradio.com/80s90s";
	protected MediaPlayer player;
	protected ILiveShowService service;
	private long timestamp;

	public interface ILiveShowService {
		void switchToNewState(LiveShowState newState);

		void goForeground(int stringId);

		void goBackground();
	}

	public LiveShowState(MediaPlayer player, ILiveShowService service) {
		this.service = service;
		this.player = player;
		this.timestamp = System.currentTimeMillis();
	}

	public abstract void enter();

	public void stopPlayback() {
		service.switchToNewState(new Idle(player, service));
	}

	public void startPlayback() {
	}

	public long getTimestamp() {
		return timestamp;
	}

	public static class Waiting extends LiveShowState {
		private String url;
		private OnPreparedListener onPrepared = new OnPreparedListener() {
			public void onPrepared(MediaPlayer mp) {
				service.switchToNewState(new Playing(player, service));
			}
		};
		private OnErrorListener onError = new OnErrorListener() {
			public boolean onError(MediaPlayer mp, int what, int extra) {
				service.switchToNewState(new Idle(player, service));
				return false;
			}
		};

		public Waiting(MediaPlayer player, ILiveShowService service, String url) {
			super(player, service);
			this.url = url;
			player.setOnPreparedListener(onPrepared);
			player.setOnErrorListener(onError);
		}

		@Override
		public void enter() {
			try {
				player.setDataSource(url);
				player.prepareAsync();
				service.goForeground(R.string.live_show_waiting);
			} catch (Exception e) {
				throw new RuntimeException(e);
			}
		}
	}

	public static class Playing extends LiveShowState {
		private OnErrorListener onError = new OnErrorListener() {
			public boolean onError(MediaPlayer mp, int what, int extra) {
				player.reset();
				service.switchToNewState(new Waiting(player, service,
						LIVE_SHOW_URL));
				return false;
			}
		};

		public Playing(MediaPlayer player, ILiveShowService service) {
			super(player, service);
			player.setOnErrorListener(onError);
		}

		@Override
		public void enter() {
			player.start();
			service.goForeground(R.string.live_show_online);
		}
	}

	public static class Idle extends LiveShowState {
		public Idle(MediaPlayer player, ILiveShowService service) {
			super(player, service);
		}

		@Override
		public void enter() {
			player.reset();
			service.goBackground();
		}

		@Override
		public void startPlayback() {
			service.switchToNewState(new Waiting(player, service, LIVE_SHOW_URL));
		}
	}
}
