package org.dandelion.radiot.podcasts.ui;

import android.content.Context;
import android.content.Intent;
import android.net.Uri;
import org.dandelion.radiot.podcasts.core.PodcastPlayer;

public class ExternalPlayer implements PodcastPlayer {
    public ExternalPlayer() {
    }

    public void startPlaying(Context context, Uri url) {
		Intent intent = new Intent(Intent.ACTION_VIEW);
		intent.setDataAndType(url, "audio/mpeg");
		context.startActivity(Intent.createChooser(intent, null));
	}
}