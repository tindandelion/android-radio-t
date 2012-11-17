package org.dandelion.radiot.live.ui;

import android.os.Bundle;
import android.view.View;
import android.widget.TextView;
import org.dandelion.radiot.R;
import org.dandelion.radiot.live.LiveShowApp;
import org.dandelion.radiot.live.LiveShowClient;
import org.dandelion.radiot.util.CustomTitleActivity;

public class LiveShowActivity extends CustomTitleActivity {
    protected LiveShowClient client;

    private String[] statusLabels;
    private LiveShowPresenter presenter;
    private TimerView timerLabel;
    private PlaybackControlFragment playbackControl;
    private View.OnClickListener onTogglePlayback = new View.OnClickListener() {
        @Override
        public void onClick(View v) {
            client.togglePlayback();
        }
    };

    @Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.live_show_screen);
		presenter = new LiveShowPresenter(this);
		statusLabels = getResources().getStringArray(
				R.array.live_show_status_labels);
        timerLabel = (TimerView) findViewById(R.id.live_timer_label);
        playbackControl = (PlaybackControlFragment) getSupportFragmentManager().findFragmentById(R.id.playback_control);
        playbackControl.setOnClickListener(onTogglePlayback);
	}

    @Override
	protected void onStart() {
		super.onStart();
        client = createClient();
        client.addListener(presenter);
	}

    private LiveShowClient createClient() {
        return LiveShowApp.getInstance().createClient(this);
    }

    @Override
	protected void onStop() {
        client.removeListener(presenter);
        timerLabel.stop();
		super.onStop();
	}

    public void setButtonState(int labelId, boolean enabled) {
        playbackControl.setButtonState(labelId, enabled);
    }

    public void setStatusLabel(int labelId) {
		TextView view = (TextView) findViewById(R.id.playback_state_label);
		view.setText(statusLabels[labelId]);
	}

    public void stopTimer() {
        timerLabel.stop();
    }

    public void startTimer(long timestamp) {
        timerLabel.start(timestamp);
    }

	public void showHelpText(boolean visible) {
		View view = findViewById(R.id.live_show_hint);
		int visibility = (visible) ? View.VISIBLE : View.INVISIBLE;
		view.setVisibility(visibility);
	}

}
