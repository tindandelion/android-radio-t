package org.dandelion.radiot.unittest.testables;

import org.dandelion.radiot.helpers.SyncValueHolder;
import org.dandelion.radiot.live.core.states.LiveShowState;
import org.dandelion.radiot.live.ui.LiveShowActivity;

public class TestableLiveShowActivity extends LiveShowActivity {
	private SyncValueHolder<Boolean> isUpdated = new SyncValueHolder<Boolean>();

	@Override
	protected void updateVisualState(LiveShowState newState) {
		setVisualStateUpdated(true);
	}

    @Override
    protected void initVisualState() {
        setVisualStateUpdated(true);
    }

    public boolean isServiceConnected() {
		return service != null;
	}

	public void resetVisualState() {
		setVisualStateUpdated(false);
	}

    public boolean isVisualStateUpdated() {
        return isUpdated.getValue();
    }

    public void setVisualStateUpdated(boolean value) {
        isUpdated.setValue(value);
    }
}
