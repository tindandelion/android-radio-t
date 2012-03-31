package org.dandelion.radiot.live.ui;

import android.app.Activity;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;
import org.dandelion.radiot.R;
import org.dandelion.radiot.home_screen.HomeScreenActivity;
import org.dandelion.radiot.live.LiveShowApp;
import org.dandelion.radiot.live.core.LiveShowStateListener;
import org.dandelion.radiot.live.core.states.LiveShowState;
import org.dandelion.radiot.live.service.LiveShowClient;

public class LiveShowActivity extends Activity {
    private LiveShowStateListener onStateChanged = new LiveShowStateListener() {
        @Override
        public void onStateChanged(LiveShowState newValue) {
            runOnUiThread(new Runnable() {
                @Override
                public void run() {
                    updateVisualState();
                }
            });
        }
    };


    protected LiveShowClient client;
	private String[] statusLabels;
	private CharSequence[] buttonLabels;
	private LiveShowPresenter presenter;
    private TimerView timerLabel;

    @Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.live_show_screen);
		presenter = new LiveShowPresenter(this);
		statusLabels = getResources().getStringArray(
				R.array.live_show_status_labels);
		buttonLabels = getResources().getStringArray(
				R.array.live_show_button_labels);
        timerLabel = (TimerView) findViewById(R.id.live_timer_label);
	}

	@Override
	protected void onStart() {
		super.onStart();
        client = createClient();
        updateVisualState();
	}

    private LiveShowClient createClient() {
        return LiveShowApp.getInstance().createClient(this, onStateChanged);
    }

    @Override
	protected void onStop() {
        client.release();
        timerLabel.stop();
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
        presenter.togglePlaybackState(client);
	}

	protected void updateVisualState() {
        client.queryState(presenter);
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
