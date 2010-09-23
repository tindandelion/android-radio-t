package org.dandelion.radiot.live;

import android.media.MediaPlayer;
import android.media.MediaPlayer.OnErrorListener;
import android.media.MediaPlayer.OnPreparedListener;

public class LiveShowState {
	private static String liveShowUrl = "http://stream3.radio-t.com:8181/stream";
//	private static String liveShowUrl = "http://icecast.bigrradio.com/80s90s";
	private static int waitTimeout = 60 * 1000;

	public static void setLiveShowUrl(String value) {
		liveShowUrl = value;
	}

	public static void setWaitTimeoutSeconds(int value) {
		waitTimeout = value * 1000;
	}

	protected MediaPlayer player;
	protected ILiveShowService service;
	private long timestamp;

	public interface ILiveShowService {
		void switchToNewState(LiveShowState newState);

		void goForeground(int stringId);

		void goBackground();

		void runAsynchronously(Runnable runnable);

		void unscheduleTimeout();

		void scheduleTimeout(int waitTimeout);

		void lockWifi();

		void unlockWifi();
	}

	public interface ILiveShowVisitor {
		void onWaiting(LiveShowState.Waiting state);

		void onIdle(LiveShowState.Idle state);

		void onConnecting(Connecting connecting);

		void onPlaying(Playing playing);

		void onStopping(Stopping stopping);
	}

	public LiveShowState(MediaPlayer player, ILiveShowService service) {
		this.service = service;
		this.player = player;
		this.timestamp = System.currentTimeMillis();
	}

	public void enter() {
	}

	public void leave() {
	}

	public void acceptVisitor(ILiveShowVisitor visitor) {
	}

	public void onTimeout() {
	}

	public void stopPlayback() {
		service.switchToNewState(new Stopping(player, service));
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
				service.switchToNewState(new Waiting(player, service));
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
				player.setDataSource(liveShowUrl);
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
		private static final int WAITING_NOTIFICATION_STRING_ID = 2;
		public Waiting(MediaPlayer player, ILiveShowService service) {
			super(player, service);
		}

		@Override
		public void enter() {
			player.reset();
			service.scheduleTimeout(waitTimeout);
			service.goForeground(WAITING_NOTIFICATION_STRING_ID);
		}
		
		@Override
		public void leave() {
			service.unscheduleTimeout();
		}

		@Override
		public void acceptVisitor(ILiveShowVisitor visitor) {
			visitor.onWaiting(this);
		}
		
		@Override
		public void onTimeout() {
			service.switchToNewState(new Connecting(player, service));
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
			service.lockWifi();
			player.start();
			service.goForeground(0);
		}
		
		@Override
		public void leave() {
			service.unlockWifi();
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

	public static class Stopping extends LiveShowState implements Runnable {
		public Stopping(MediaPlayer player, ILiveShowService service) {
			super(player, service);
		}

		@Override
		public void enter() {
			service.runAsynchronously(this);
		}

		@Override
		public void stopPlayback() {
		}

		@Override
		public void acceptVisitor(ILiveShowVisitor visitor) {
			visitor.onStopping(this);
		}

		public void run() {
			player.reset();
			service.switchToNewState(new Idle(player, service));
		}

	}
}
