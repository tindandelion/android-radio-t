package org.dandelion.radiot;

import android.content.Context;
import android.content.Intent;
import android.net.Uri;

class ExternalPlayer implements IPodcastPlayer {
	private Context context;

	public ExternalPlayer(Context context) {
		this.context = context;
	}
	
	public void startPlaying(Uri url) {
		Intent intent = new Intent(Intent.ACTION_VIEW);
		intent.setDataAndType(url, "audio/mpeg");
		context.startActivity(intent);
	}
}