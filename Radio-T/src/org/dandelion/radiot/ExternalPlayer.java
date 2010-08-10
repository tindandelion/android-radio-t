package org.dandelion.radiot;

import android.content.Context;
import android.content.Intent;
import android.net.Uri;

class ExternalPlayer implements IPodcastPlayer {
	public void startPlaying(Context context, Uri url) {
		Intent intent = new Intent(Intent.ACTION_VIEW);
		intent.setDataAndType(url, "audio/mpeg");
		context.startActivity(Intent.createChooser(intent, null));
	}

	public void stop() {
	}
}