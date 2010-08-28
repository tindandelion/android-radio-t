package org.dandelion.radiot.live;

import org.dandelion.radiot.R;

import android.media.MediaPlayer;
import android.media.MediaPlayer.OnErrorListener;
import android.media.MediaPlayer.OnPreparedListener;

public abstract class LiveShowState {
	private static final String LIVE_SHOW_URL = "http://icecast.bigrradio.com/80s90s";
	protected MediaPlayer player;
	protected ILiveShowService service;

	public interface ILiveShowService {
		void switchToNewState(LiveShowState newState);

		void goForeground(int stringId);

		void goBackground();
	}

	public LiveShowState(MediaPlayer player, ILiveShowService service) {
		this.service = service;
		this.player = player;
	}

	public abstract void enter();

	public abstract StateNames getName();

	public void stopPlayback() {
		service.switchToNewState(new Idle(player, service));
	}

	public void startPlayback() {
	}

	public enum StateNames {
		Waiting, Playing, Idle
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

		@Override
		public StateNames getName() {
			return StateNames.Waiting;
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

		@Override
		public StateNames getName() {
			return StateNames.Playing;
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
		public StateNames getName() {
			return StateNames.Idle;
		}

		@Override
		public void startPlayback() {
			service.switchToNewState(new Waiting(player, service, LIVE_SHOW_URL));
		}
	}
}
