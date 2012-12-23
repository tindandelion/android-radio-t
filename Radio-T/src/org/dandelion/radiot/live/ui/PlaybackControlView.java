package org.dandelion.radiot.live.ui;

import android.content.Context;
import android.util.AttributeSet;
import android.view.LayoutInflater;
import android.widget.ImageButton;
import android.widget.LinearLayout;
import android.widget.TextView;
import org.dandelion.radiot.R;
import org.dandelion.radiot.live.core.LiveShowState;
import org.dandelion.radiot.live.core.LiveShowStateListener;

public class PlaybackControlView extends LinearLayout implements LiveShowStateListener {

    private final String[] statusLabels;

    private final ImageButton button;
    private final TextView status;
    private TimerView timer;

    public PlaybackControlView(Context context, AttributeSet attrs) {
        super(context, attrs);
        inflateView(context);
        statusLabels = initStatusLabels();
        button = (ImageButton) findViewById(R.id.btn_toggle_live_playback);
        status = (TextView) findViewById(R.id.playback_state_label);
        timer = (TimerView) findViewById(R.id.live_timer_label);
    }

    private String[] initStatusLabels() {
        return getResources().getStringArray(R.array.live_show_status_labels);
    }

    private void inflateView(Context context) {
        LayoutInflater inflater = (LayoutInflater) context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
        inflater.inflate(R.layout.playback_control_view, this);
    }

    public void setButtonListener(OnClickListener listener) {
        button.setOnClickListener(listener);
    }

    @Override
    public void onStateChanged(LiveShowState state, long timestamp) {
        updateButtonForState(state);
        updateTimer(state, timestamp);
        updateLabel(state);
    }

    private void updateLabel(LiveShowState state) {
        status.setText(statusLabels[state.ordinal()]);
    }

    private void updateTimer(LiveShowState state, long timestamp) {
        if (state == LiveShowState.Idle) {
            timer.stop();
        } else {
            timer.start(timestamp);
        }
    }

    private void updateButtonForState(LiveShowState state) {
        int iconId = (state == LiveShowState.Idle) ? R.drawable.ic_play : R.drawable.ic_stop;
        boolean enabled = (state != LiveShowState.Stopping);
        button.setImageResource(iconId);
        button.setEnabled(enabled);
    }
}
