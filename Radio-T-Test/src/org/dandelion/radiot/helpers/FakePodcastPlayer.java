package org.dandelion.radiot.helpers;

import junit.framework.Assert;

import org.dandelion.radiot.IPodcastPlayer;

import android.content.Context;
import android.net.Uri;

public class FakePodcastPlayer implements IPodcastPlayer {

	private Uri podcastBeingPlayed;

	public void startPlaying(Context context, Uri url) {
		podcastBeingPlayed = url;
	}

	public void assertIsPlaying(Uri url) throws Exception {
		Assert.assertEquals(url, podcastBeingPlayed);
	}

	public void assertStopped() {
		Assert.assertNull(podcastBeingPlayed);
	}

	public void stop() {
		podcastBeingPlayed = null;
	}

}