package org.dandelion.radiot;

import android.app.Activity;
import android.os.Bundle;

public class OnAirActivity extends Activity {
	public static final String LIVE_SHOW_URL = "http://stream.radio-t.com:8181/stream.m3u";

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.on_air_screen);
	}
}
