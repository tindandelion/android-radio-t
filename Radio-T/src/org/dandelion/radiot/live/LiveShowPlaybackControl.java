package org.dandelion.radiot.live;

import org.dandelion.radiot.R;

import android.content.Context;
import android.text.format.DateUtils;
import android.util.AttributeSet;
import android.view.LayoutInflater;
import android.widget.Button;
import android.widget.RelativeLayout;
import android.widget.TextView;

public class LiveShowPlaybackControl extends RelativeLayout {
	private TextView statusLabel;
	private String[] statusLabels;
	private TextView timerLabel;
	private Button button;
	private String[] buttonLabels;

	public LiveShowPlaybackControl(Context context, AttributeSet attrs) {
		super(context, attrs);
		inflateLayout();
		initResources();
	}

	private void initResources() {
		statusLabels = getResources().getStringArray(
				R.array.live_show_status_labels);
		buttonLabels = getResources().getStringArray(
				R.array.live_show_button_labels);
	}

	private void inflateLayout() {
		LayoutInflater inflater = (LayoutInflater) getContext()
				.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
		inflater.inflate(R.layout.live_show_playback_control, this);

		statusLabel = (TextView) findViewById(R.id.playback_state_label);
		timerLabel = (TextView) findViewById(R.id.live_timer_label);
		button = (Button) findViewById(R.id.live_show_action_button);
	}
	
	// Called externally from the activity, until the logic is moved here

	public void setStatusLabel(int labelId) {
		statusLabel.setText(statusLabels[labelId]);
	}

	public void setElapsedTime(long seconds) {
		timerLabel.setText(DateUtils.formatElapsedTime(seconds));
	}

	public void onButtonClick(OnClickListener l) {
		button.setOnClickListener(l);
	}

	public void setButtonState(int labelId, boolean enabled) {
		button.setText(buttonLabels[labelId]);
		button.setEnabled(enabled);
	}
}
