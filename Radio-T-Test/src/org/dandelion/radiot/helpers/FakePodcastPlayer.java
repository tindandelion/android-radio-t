package org.dandelion.radiot.helpers;

import android.content.Context;
import junit.framework.Assert;
import org.dandelion.radiot.podcasts.core.PodcastPlayer;

public class FakePodcastPlayer implements PodcastPlayer {
    private SyncValueHolder<String> podcastToPlay = new SyncValueHolder<String>();

	public void startPlaying(Context context, String url) {
        podcastToPlay.setValue(url);
	}

	public void assertIsPlaying(String url) throws Exception {
		Assert.assertEquals(url, podcastToPlay.getValue());
	}
}