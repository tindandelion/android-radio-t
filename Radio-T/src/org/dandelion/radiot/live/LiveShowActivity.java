package org.dandelion.radiot.live;

import org.dandelion.radiot.R;
import org.dandelion.radiot.home_screen.HomeScreenActivity;

import android.app.Activity;
import android.content.BroadcastReceiver;
import android.content.ComponentName;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.content.ServiceConnection;
import android.os.Bundle;
import android.os.IBinder;
import android.text.format.DateUtils;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

public class LiveShowActivity extends Activity {
	protected LiveShowService service;

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
	private String[] statusLabels;
	private CharSequence[] buttonLabels;
	private LiveShowPresenter visitor;
	
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.live_show_screen);
		visitor = new LiveShowPresenter(this);
		statusLabels = getResources().getStringArray(
				R.array.live_show_status_labels);
		buttonLabels = getResources().getStringArray(
				R.array.live_show_button_labels);
	}

	@Override
	protected void onStart() {
		super.onStart();
		Intent i = new Intent(this, LiveShowService.class);
		startService(i);
		bindService(i, onService, 0);
		registerReceiver(onPlaybackState, new IntentFilter(
				LiveShowService.PLAYBACK_STATE_CHANGED));
	}

	@Override
	protected void onStop() {
		unregisterReceiver(onPlaybackState);
		unbindService(onService);
		service = null;
		visitor.stopTimer();
		super.onStop();
	}

	@Override
	public boolean onCreateOptionsMenu(Menu menu) {
		getMenuInflater().inflate(R.menu.default_menu, menu);
		return super.onCreateOptionsMenu(menu);
	}

	@Override
	public boolean onOptionsItemSelected(MenuItem item) {
		if (item.getItemId() == R.id.go_home) {
			HomeScreenActivity.start(this);
		}
		return super.onOptionsItemSelected(item);
	}

	public void onButtonPressed(View v) {
		visitor.switchPlaybackState(service.getCurrentState());
	}

	protected void updateVisualState() {
		if (service != null)
			service.acceptVisitor(visitor);
	}

	public LiveShowService getService() {
		return service;
	}

	public void setButtonState(int labelId, boolean enabled) {
		Button button = (Button) findViewById(R.id.live_show_action_button);
		button.setText(buttonLabels[labelId]);
		button.setEnabled(enabled);
	}

	public void setStatusLabel(int labelId) {
		TextView view = (TextView) findViewById(R.id.playback_state_label);
		view.setText(statusLabels[labelId]);
	}

	public void setElapsedTime(long seconds) {
		TextView timerLabel = (TextView) findViewById(R.id.live_timer_label);
		timerLabel.setText(DateUtils.formatElapsedTime(seconds));
	}

	public void showWaitingHint() {
		Toast.makeText(this, R.string.live_show_waiting_hint,
				Toast.LENGTH_SHORT).show();
	}
}
