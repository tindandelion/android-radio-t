package org.dandelion.radiot.podcasts.core;

import android.content.Context;

public interface PodcastProcessor {
    // TODO: Having context here is ugly
	void process(Context context, String url);
}
