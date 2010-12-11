package org.dandelion.radiot.live;

import org.dandelion.radiot.R;

import android.app.Activity;
import android.content.BroadcastReceiver;
import android.content.ComponentName;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.content.ServiceConnection;
import android.os.IBinder;
import android.text.format.DateUtils;
import android.util.AttributeSet;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.Button;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

public class LiveShowPlaybackControl extends RelativeLayout implements
		ILiveShowPlaybackView {
	private TextView statusLabel;
	private String[] statusLabels;
	private TextView timerLabel;
	private Button button;
	private String[] buttonLabels;
	private LiveShowPresenter presenter;
	private LiveShowService service;
	
	private ServiceConnection onService = new ServiceConnection() {
		public void onServiceDisconnected(ComponentName name) {
		}

		public void onServiceConnected(ComponentName name, IBinder binder) {
			service = ((LiveShowService.LocalBinder) binder).getService();
			updateVisualState();
		}
	};
	
	private OnClickListener onButtonClick = new OnClickListener() {
		public void onClick(View v) {
			presenter.switchPlaybackState(service.getCurrentState());
		}
	};

	public LiveShowPlaybackControl(Context context, AttributeSet attrs) {
		super(context, attrs);
		inflateLayout();
		initResources();
		presenter = new LiveShowPresenter(this);
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
		button.setOnClickListener(onButtonClick);
	}

	// Called externally from the activity, until the logic is moved here

	public void setStatusLabel(int labelId) {
		statusLabel.setText(statusLabels[labelId]);
	}

	public void setElapsedTime(final long seconds) {
		// TODO: Get rid of dependency to activity
		Activity activity = (Activity) getContext();
		activity.runOnUiThread(new Runnable() {
			public void run() {
				timerLabel.setText(DateUtils.formatElapsedTime(seconds));
			}
		});
	}

	public void setButtonState(int labelId, boolean enabled) {
		button.setText(buttonLabels[labelId]);
		button.setEnabled(enabled);
	}

	public void showWaitingHint() {
		Toast.makeText(getContext(), R.string.live_show_waiting_hint,
				Toast.LENGTH_SHORT).show();
	}

	public void onStart(BroadcastReceiver onPlaybackState) {
		Context context = getContext();
		Intent i = new Intent(context, LiveShowService.class);
		context.startService(i);
		context.bindService(i, onService, 0);
		context.registerReceiver(onPlaybackState, new IntentFilter(
				LiveShowService.PLAYBACK_STATE_CHANGED));
	}

	public void onStop(BroadcastReceiver onPlaybackState) {
		Context context = getContext();
		context.unregisterReceiver(onPlaybackState);
		context.unbindService(onService);
		service = null;
		presenter.stopTimer();
	}

	public LiveShowService getService() {
		return service;
	}

	public void setService(LiveShowService service) {
		this.service = service;
	}

	public void updateVisualState() {
		if (service != null)
			service.acceptVisitor(presenter);
	}
}
