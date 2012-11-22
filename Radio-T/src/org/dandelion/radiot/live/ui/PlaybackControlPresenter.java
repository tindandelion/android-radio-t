package org.dandelion.radiot.live.ui;

import org.dandelion.radiot.live.core.LiveShowState;
import org.dandelion.radiot.live.core.LiveShowStateListener;

import java.util.HashMap;
import java.util.Map;

import static org.dandelion.radiot.live.core.LiveShowState.*;

public class PlaybackControlPresenter implements LiveShowStateListener {
    private PlaybackControlFragment control;

    private static class VisualState {
        private final int statusLabelId;
        private final Boolean showHelpText;
        private final int buttonLabelId;
        private final Boolean buttonEnabled;
        private final Boolean timerActive;

        private VisualState(int statusLabelId, Boolean showHelpText,
                            int buttonLabelId, Boolean buttonEnabled, Boolean timerActive) {
            this.statusLabelId = statusLabelId;
            this.showHelpText = showHelpText;
            this.buttonLabelId = buttonLabelId;
            this.buttonEnabled = buttonEnabled;
            this.timerActive = timerActive;
        }

        public void update(PlaybackControlFragment control, long timestamp) {
            control.setStatusLabel(statusLabelId);
            control.setButtonState(buttonLabelId, buttonEnabled);
            control.showHelpText(showHelpText);
            if (timerActive) {
                control.startTimer(timestamp);
            } else {
                control.stopTimer();
            }
        }
    }
    private static Map<LiveShowState, VisualState> stateMap = new HashMap<LiveShowState, VisualState>();
    static {
        stateMap.put(Idle, new VisualState(0, false, 1, true, false));
        stateMap.put(Connecting, new VisualState(1, false, 0, true, true));
        stateMap.put(Playing, new VisualState(2, false, 0, true, true));
        stateMap.put(Stopping, new VisualState(3, false, 0, false, true));
        stateMap.put(Waiting, new VisualState(4, true, 0, true, true));
    }

    public PlaybackControlPresenter(PlaybackControlFragment control) {
        this.control = control;
	}

    @Override
    public void onStateChanged(LiveShowState state, long timestamp) {
        final VisualState visualState = stateMap.get(state);
        visualState.update(control, timestamp);
    }
}
