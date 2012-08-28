package org.dandelion.radiot.live.ui;

import org.dandelion.radiot.live.core.LiveShowState;
import org.dandelion.radiot.live.core.LiveShowStateListener;

import java.util.HashMap;
import java.util.Map;

import static org.dandelion.radiot.live.core.LiveShowState.*;

public class LiveShowPresenter implements LiveShowStateListener {
    private static Map<LiveShowState, VisualState> stateMap = new HashMap<LiveShowState, VisualState>();

    static {
        stateMap.put(Idle, new VisualState(0, false, 1, true, false));
        stateMap.put(Connecting, new VisualState(1, false, 0, true, true));
        stateMap.put(Playing, new VisualState(2, false, 0, true, true));
        stateMap.put(Stopping, new VisualState(3, false, 0, false, true));
        stateMap.put(Waiting, new VisualState(4, true, 0, true, true));
    }

    private LiveShowFragment view;

    public LiveShowPresenter(LiveShowFragment view) {
        this.view = view;
	}

    @Override
    public void onStateChanged(LiveShowState state, long timestamp) {
        final VisualState visualState = stateMap.get(state);
        visualState.update(view, timestamp);
    }

    private static class VisualState {
        public final int statusLabelId;
        public final Boolean showHelpText;
        public final int buttonLabelId;
        public final Boolean buttonEnabled;
        public final Boolean timerActive;

        private VisualState(int statusLabelId, Boolean showHelpText,
                            int buttonLabelId, Boolean buttonEnabled, Boolean timerActive) {
            this.statusLabelId = statusLabelId;
            this.showHelpText = showHelpText;
            this.buttonLabelId = buttonLabelId;
            this.buttonEnabled = buttonEnabled;
            this.timerActive = timerActive;
        }

        public void update(LiveShowFragment view, long timestamp) {
            view.setStatusLabel(statusLabelId);
            view.setButtonState(buttonLabelId, buttonEnabled);
            view.showHelpText(showHelpText);
            if (timerActive) {
                view.startTimer(timestamp);
            } else {
                view.stopTimer();
            }
        }
    }
}
