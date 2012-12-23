package org.dandelion.radiot.live.ui;

import android.widget.TextView;
import org.dandelion.radiot.R;
import android.content.Context;
import android.util.AttributeSet;
import android.widget.ImageButton;
import android.widget.LinearLayout;

public class PlaybackControlView extends LinearLayout {
    public PlaybackControlView(Context context, AttributeSet attrs) {
        super(context, attrs);
    }

    public ImageButton button() {
        return (ImageButton) findViewById(R.id.btn_toggle_live_playback);
    }

    public TextView status() {
        return (TextView) findViewById(R.id.playback_state_label);
    }

    public TimerView timer() {
        return (TimerView) findViewById(R.id.live_timer_label);
    }
}
