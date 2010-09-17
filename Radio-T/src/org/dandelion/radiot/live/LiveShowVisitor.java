package org.dandelion.radiot.live;

import org.dandelion.radiot.live.LiveShowState.Connecting;
import org.dandelion.radiot.live.LiveShowState.ILiveShowVisitor;
import org.dandelion.radiot.live.LiveShowState.Idle;
import org.dandelion.radiot.live.LiveShowState.Playing;
import org.dandelion.radiot.live.LiveShowState.Waiting;

public class LiveShowVisitor implements ILiveShowVisitor {

	private LiveShowActivity activity;

	public LiveShowVisitor(LiveShowActivity activity) {
		this.activity = activity;
	}

	public void onIdle(Idle state) {
		setStateLabel(0);
		setButtonLabel(1);
		showHelpText(false);
	}

	public void onWaiting(Waiting state) {
		setStateLabel(2);
		setButtonLabel(0);
		showHelpText(true);
	}
	
	public void onConnecting(Connecting connecting) {
		setStateLabel(1);
		setButtonLabel(0);
		showHelpText(false);
	}

	public void onPlaying(Playing playing) {
		setStateLabel(3);
		setButtonLabel(0);
		showHelpText(false);
	}

	private void setStateLabel(int stringId) {
		activity.setStatusLabel(stringId);
	}
	
	private void setButtonLabel(int stringId) {
		activity.setButtonLabel(stringId);
	}
	
	private void showHelpText(boolean b) {
		activity.showHelpText(b);
	}
}
