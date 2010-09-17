package org.dandelion.radiot.live;


public class LiveShowPresenter {
	public static final LiveShowPresenter Null = new LiveShowPresenter(null,
			null);

	protected LiveShowActivity activity;
	protected LiveShowState showState;

	private LiveShowPresenter(LiveShowState state, LiveShowActivity activity) {
		this.showState = state;
		this.activity = activity;
	}

	public static LiveShowPresenter create(LiveShowState state,
			LiveShowActivity activity) {
		if (state instanceof LiveShowState.Idle) {
			return new InactiveStatePresenter(state, activity);
		} else {
			return new ActiveStatePresenter(state, activity);
		}
	}

	public void switchPlaybackState() {
	}

	static class ActiveStatePresenter extends LiveShowPresenter {

		public ActiveStatePresenter(LiveShowState state,
				LiveShowActivity activity) {
			super(state, activity);
		}

		@Override
		public void switchPlaybackState() {
			showState.stopPlayback();
		}
	}

	static class InactiveStatePresenter extends LiveShowPresenter {
		public InactiveStatePresenter(LiveShowState state,
				LiveShowActivity activity) {
			super(state, activity);
		}

		@Override
		public void switchPlaybackState() {
			showState.startPlayback();
		}
	}
}
