package org.dandelion.radiot.unittest.testables;

import org.dandelion.radiot.live.core.states.LiveShowState;
import org.dandelion.radiot.live.ui.LiveShowActivity;

public class TestableLiveShowActivity extends LiveShowActivity {
	public boolean isVisualStateUpdated = false;

	@Override
	protected void updateVisualState(LiveShowState newState) {
		isVisualStateUpdated = true;
	}

    @Override
    protected void initVisualState() {
        isVisualStateUpdated = true;
    }

    public boolean isServiceConnected() {
		return service != null;
	}

	public void resetVisualState() {
		isVisualStateUpdated = false;
	}

}
