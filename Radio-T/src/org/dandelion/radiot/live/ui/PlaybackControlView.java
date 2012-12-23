package org.dandelion.radiot.live.ui;

import android.content.Context;
import android.util.AttributeSet;
import android.view.LayoutInflater;
import android.widget.ImageButton;
import android.widget.LinearLayout;
import android.widget.TextView;
import org.dandelion.radiot.R;

public class PlaybackControlView extends LinearLayout {
    private static final int[] BUTTON_ICONS = new int[]{
            R.drawable.ic_stop,
            R.drawable.ic_play
    };

    private final String [] statusLabels;

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

    public void setButtonState(int resourceId, boolean enabled) {
        button.setImageResource(BUTTON_ICONS[resourceId]);
        button.setEnabled(enabled);
    }

    public void setStatusLabel(int id) {
        status.setText(statusLabels[id]);
    }

    public void stopTimer() {
        timer.stop();
    }

    public void startTimer(long timestamp) {
        timer.start(timestamp);
    }
}
