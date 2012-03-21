package org.dandelion.radiot.podcasts.core;

import android.content.Context;

public interface PodcastProcessor {
	void process(Context context, PodcastItem podcast);
}
