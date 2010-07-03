package org.dandelion.radiot;

import android.app.Activity;
import android.net.Uri;
import android.os.Bundle;
import android.view.View;

public class LiveShowActivity extends Activity {
	public static final Uri LIVE_SHOW_URL = Uri.parse("http://stream.radio-t.com:8181/stream.m3u");
	private IPodcastPlayer podcastPlayer;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.live_show_screen);
		setPodcastPlayer(new ExternalPlayer(this));
	}

	public void setPodcastPlayer(
			IPodcastPlayer player) {
		podcastPlayer = player;
	}
	
	public void listenButtonClicked(View button) {
		podcastPlayer.startPlaying(LIVE_SHOW_URL);
	}
}
