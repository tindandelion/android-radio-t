package org.dandelion.radiot.live;

import java.util.Timer;
import java.util.TimerTask;

import android.media.MediaPlayer;
import android.media.MediaPlayer.OnErrorListener;
import android.media.MediaPlayer.OnPreparedListener;

public abstract class LiveShowState {
//	private static final String LIVE_SHOW_URL = "http://stream3.radio-t.com:8181/stream";
	 private static final String LIVE_SHOW_URL =
	 "http://icecast.bigrradio.com/80s90s";
	private static final long WAIT_TIMEOUT = 60 * 1000;

	protected MediaPlayer player;
	protected ILiveShowService service;
	private long timestamp;

	public interface ILiveShowService {
		void switchToNewState(LiveShowState newState);

		void goForeground(int stringId);

		void goBackground();
	}

	public interface ILiveShowVisitor {
		void onWaiting(LiveShowState.Waiting state);
		void onIdle(LiveShowState.Idle state);
		void onConnecting(Connecting connecting);
		void onPlaying(Playing playing);
	}

	public LiveShowState(MediaPlayer player, ILiveShowService service) {
		this.service = service;
		this.player = player;
		this.timestamp = System.currentTimeMillis();
	}

	public abstract void enter();
	public abstract void acceptVisitor(ILiveShowVisitor visitor);

	public void stopPlayback() {
		service.switchToNewState(new Idle(player, service));
	}

	public void startPlayback() {
	}

	public long getTimestamp() {
		return timestamp;
	}


	public static class Connecting extends LiveShowState {
		private OnPreparedListener onPrepared = new OnPreparedListener() {
			public void onPrepared(MediaPlayer mp) {
				service.switchToNewState(new Playing(player, service));
			}
		};
		private OnErrorListener onError = new OnErrorListener() {
			public boolean onError(MediaPlayer mp, int what, int extra) {
				service.switchToNewState(new Waiting(player, service,
						new Timer()));
				return false;
			}
		};

		public Connecting(MediaPlayer player, ILiveShowService service) {
			super(player, service);
			player.setOnPreparedListener(onPrepared);
			player.setOnErrorListener(onError);
		}

		@Override
		public void enter() {
			try {
				player.reset();
				player.setDataSource(LIVE_SHOW_URL);
				player.prepareAsync();
				service.goForeground(1);
			} catch (Exception e) {
				throw new RuntimeException(e);
			}
		}

		@Override
		public void acceptVisitor(ILiveShowVisitor visitor) {
			visitor.onConnecting(this);
		}
	}

	public static class Waiting extends LiveShowState {
		private Timer timer;
		private TimerTask task = new TimerTask() {
			public void run() {
				service.switchToNewState(new Connecting(player, service));
			}
		};

		public Waiting(MediaPlayer player, ILiveShowService service, Timer timer) {
			super(player, service);
			this.timer = timer;
		}

		@Override
		public void enter() {
			player.reset();
			timer.schedule(task, WAIT_TIMEOUT);
			service.goForeground(2);
		}

		@Override
		public void stopPlayback() {
			timer.cancel();
			super.stopPlayback();
		}

		@Override
		public void acceptVisitor(ILiveShowVisitor visitor) {
			visitor.onWaiting(this);
		}

	}

	public static class Playing extends LiveShowState {
		private OnErrorListener onError = new OnErrorListener() {
			public boolean onError(MediaPlayer mp, int what, int extra) {
				player.reset();
				service.switchToNewState(new Connecting(player, service));
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
			service.goForeground(0);
		}

		@Override
		public void acceptVisitor(ILiveShowVisitor visitor) {
			visitor.onPlaying(this);
		}
	}

	public static class Idle extends LiveShowState {
		public Idle(MediaPlayer player, ILiveShowService service) {
			super(player, service);
			player.setOnErrorListener(null);
		}

		@Override
		public void enter() {
			player.reset();
			service.goBackground();
		}

		@Override
		public void startPlayback() {
			service.switchToNewState(new Connecting(player, service));
		}

		@Override
		public void acceptVisitor(ILiveShowVisitor visitor) {
			visitor.onIdle(this);
		}
	}
}
