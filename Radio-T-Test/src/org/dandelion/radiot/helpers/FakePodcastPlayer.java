package org.dandelion.radiot.helpers;

import android.content.Context;
import junit.framework.Assert;

import org.dandelion.radiot.podcasts.core.PodcastPlayer;

import android.net.Uri;

public class FakePodcastPlayer implements PodcastPlayer {

	private Uri podcastBeingPlayed;

	public void startPlaying(Context context, Uri url) {
		podcastBeingPlayed = url;
	}

	public void assertIsPlaying(Uri url) throws Exception {
		Assert.assertEquals(url, podcastBeingPlayed);
	}

}