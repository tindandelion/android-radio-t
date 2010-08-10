package org.dandelion.radiot;

import android.net.Uri;
import android.os.Bundle;

public class LiveShowScreen extends RadiotActivity {
//	public static final Uri LIVE_SHOW_URL = Uri
//			.parse("http://stream3.radio-t.com:8181/stream");
	
	public static final Uri LIVE_SHOW_URL = Uri
	.parse("http://icecast.bigrradio.com/80s90s");
	
	private IPodcastPlayer podcastPlayer;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.live_show_screen);
		podcastPlayer = ((RadiotApplication)getApplication()).getMediaPlayer(this);
	}
	
	@Override
	protected void onResume() {
		super.onResume();
		podcastPlayer.startPlaying(this, LIVE_SHOW_URL);
	}
}
