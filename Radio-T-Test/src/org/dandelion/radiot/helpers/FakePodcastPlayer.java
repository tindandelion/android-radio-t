package org.dandelion.radiot.helpers;

import android.content.Context;
import junit.framework.Assert;
import org.dandelion.radiot.podcasts.core.PodcastProcessor;

public class FakePodcastPlayer implements PodcastProcessor {
    private SyncValueHolder<String> podcastToPlay = new SyncValueHolder<String>();

	public void process(Context context, String url) {
        podcastToPlay.setValue(url);
	}

	public void assertIsPlaying(String url) throws Exception {
		Assert.assertEquals(url, podcastToPlay.getValue());
	}
}