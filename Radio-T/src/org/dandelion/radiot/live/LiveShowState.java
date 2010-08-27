package org.dandelion.radiot.live;

import android.media.MediaPlayer;
import android.media.MediaPlayer.OnPreparedListener;

public abstract class LiveShowState {
	private static final String LIVE_SHOW_URL = "http://icecast.bigrradio.com/80s90s";
	protected MediaPlayer player;
	protected LiveShowService service;

	public LiveShowState(MediaPlayer player, LiveShowService service) {
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

		public Waiting(MediaPlayer player, LiveShowService service, String url) {
			super(player, service);
			this.url = url;
		}

		@Override
		public void enter() {
			try {
				player.setDataSource(url);
				player.setOnPreparedListener(onPrepared);
				player.prepareAsync();
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
		public Playing(MediaPlayer player, LiveShowService service) {
			super(player, service);
		}

		@Override
		public void enter() {
			player.start();
		}

		@Override
		public StateNames getName() {
			return StateNames.Playing;
		}
	}

	public static class Idle extends LiveShowState {
		public Idle(MediaPlayer player, LiveShowService service) {
			super(player, service);
		}

		@Override
		public void enter() {
			player.reset();
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
