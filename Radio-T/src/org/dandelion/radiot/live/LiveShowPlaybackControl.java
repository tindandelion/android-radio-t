package org.dandelion.radiot.live;

import org.dandelion.radiot.R;

import android.content.BroadcastReceiver;
import android.content.ComponentName;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.content.ServiceConnection;
import android.os.IBinder;
import android.util.AttributeSet;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.ImageButton;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

public class LiveShowPlaybackControl extends RelativeLayout implements
		ILiveShowPlaybackView {
	private static int[] buttonLabels = { 
		R.drawable.ic_stop,
		R.drawable.ic_play
	};
	private TextView statusLabel;
	private String[] statusLabels;
	private ImageButton button;
	private LiveShowPresenter presenter;
	private LiveShowService service;

	protected BroadcastReceiver onPlaybackState = new BroadcastReceiver() {
		public void onReceive(Context context, Intent intent) {
			updateVisualState();
		}
	};

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
	}

	private void inflateLayout() {
		LayoutInflater inflater = (LayoutInflater) getContext()
				.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
		inflater.inflate(R.layout.live_show_playback_control, this);

		statusLabel = (TextView) findViewById(R.id.playback_state_label);
		button = (ImageButton) findViewById(R.id.live_show_action_button);
		button.setOnClickListener(onButtonClick);
	}

	public void setStatusLabel(int labelId) {
		statusLabel.setText(statusLabels[labelId]);
	}

	public void setElapsedTime(final long seconds) {
	}

	public void setButtonState(int labelId, boolean enabled) {
		button.setImageResource(buttonLabels[labelId]);
		button.setEnabled(enabled);
	}

	public void showWaitingHint() {
		Toast.makeText(getContext(), R.string.live_show_waiting_hint,
				Toast.LENGTH_SHORT).show();
	}

	public void onStart() {
		Context context = getContext();
		Intent i = new Intent(context, LiveShowService.class);
		context.startService(i);
		context.bindService(i, onService, 0);
		context.registerReceiver(this.onPlaybackState, new IntentFilter(
				LiveShowService.PLAYBACK_STATE_CHANGED));
	}

	public void onStop() {
		Context context = getContext();
		context.unregisterReceiver(this.onPlaybackState);
		context.unbindService(onService);
		service = null;
		presenter.stopTimer();
	}

	public void stopPlayback() {
		service.stopPlayback();
	}

	public void updateVisualState() {
		if (service != null)
			service.acceptVisitor(presenter);
	}
}
