package org.dandelion.radiot.live;

import org.dandelion.radiot.R;

import android.app.Activity;
import android.os.Bundle;

public class LiveShowActivity extends Activity {

	// public static final String LIVE_SHOW_URL =
	// "http://stream3.radio-t.com:8181/stream";
	public static final String LIVE_SHOW_URL = "http://icecast.bigrradio.com/80s90s";
	
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.live_show_screen);
	}
}
