package org.dandelion.radiot.live;

import org.dandelion.radiot.R;
import org.dandelion.radiot.home_screen.HomeScreenActivity;

import android.app.Activity;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;

public class LiveShowActivity extends Activity {
	protected BroadcastReceiver onPlaybackState = new BroadcastReceiver() {
		public void onReceive(Context context, Intent intent) {
			updateVisualState();
		}
	};
	
	private LiveShowPlaybackControl playbackControl;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.live_show_screen);
		playbackControl = (LiveShowPlaybackControl) findViewById(R.id.live_show_playback_control);
	}

	@Override
	protected void onStart() {
		super.onStart();
		playbackControl.onStart(onPlaybackState);
	}

	@Override
	protected void onStop() {
		playbackControl.onStop(onPlaybackState);
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

	protected void updateVisualState() {
		playbackControl.updateVisualState();
	}
	
	public LiveShowService getService() { 
		return playbackControl.getService();
	}

}
