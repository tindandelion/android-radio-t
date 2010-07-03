package org.dandelion.radiot;

import android.app.Activity;
import android.os.Bundle;
import android.view.View;

public class OnAirActivity extends Activity implements IPodcastPlayer {
	public static final String LIVE_SHOW_URL = "http://stream.radio-t.com:8181/stream.m3u";
	private IPodcastPlayer podcastPlayer;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.on_air_screen);
		setPodcastPlayer(this);
	}

	public void setPodcastPlayer(
			IPodcastPlayer player) {
		podcastPlayer = player;
	}
	
	public void listenButtonClicked(View button) {
		podcastPlayer.startPlaying(LIVE_SHOW_URL);
	}

	public void startPlaying(String url) {
	}
}
