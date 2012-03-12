package org.dandelion.radiot.podcasts.core;

import android.content.Context;
import android.net.Uri;

public interface PodcastPlayer {
	void startPlaying(Context context, Uri url);
}
