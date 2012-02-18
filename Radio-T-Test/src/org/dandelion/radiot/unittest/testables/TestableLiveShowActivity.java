package org.dandelion.radiot.unittest.testables;

import org.dandelion.radiot.live.LiveShowActivity;

public class TestableLiveShowActivity extends LiveShowActivity {
	private boolean isVisualStateUpdated = false;

	@Override
	protected void updateVisualState() {
		isVisualStateUpdated = true;
	}

	public boolean isVisualStateUpdated() {
		return isVisualStateUpdated;
	}

	public boolean isServiceConnected() {
		return service != null;
	}

	public void resetVisualState() {
		isVisualStateUpdated = false;
	}

}
