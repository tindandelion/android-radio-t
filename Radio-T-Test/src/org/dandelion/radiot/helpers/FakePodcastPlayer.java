package org.dandelion.radiot.helpers;

import android.content.Context;
import junit.framework.Assert;

import org.dandelion.radiot.podcasts.core.PodcastPlayer;

import android.net.Uri;

public class FakePodcastPlayer implements PodcastPlayer {
    private SyncValueHolder<Uri> podcastToPlay = new SyncValueHolder<Uri>();

	public void startPlaying(Context context, Uri url) {
        podcastToPlay.setValue(url);
	}

	public void assertIsPlaying(Uri url) throws Exception {
		Assert.assertEquals(url, podcastToPlay.getValue());
	}
}