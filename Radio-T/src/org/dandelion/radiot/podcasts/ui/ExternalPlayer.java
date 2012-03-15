package org.dandelion.radiot.podcasts.ui;

import android.content.Context;
import android.content.Intent;
import android.net.Uri;
import org.dandelion.radiot.podcasts.core.PodcastProcessor;

public class ExternalPlayer implements PodcastProcessor {
    public ExternalPlayer() {
    }

    public void process(Context context, String url) {
		Intent intent = new Intent(Intent.ACTION_VIEW);
		intent.setDataAndType(Uri.parse(url), "audio/mpeg");
		context.startActivity(Intent.createChooser(intent, null));
	}
}