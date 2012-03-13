package org.dandelion.radiot.helpers;

import junit.framework.Assert;
import org.dandelion.radiot.podcasts.core.PodcastDownloader;
import org.dandelion.radiot.podcasts.core.PodcastItem;

public class FakePodcastDownloader implements PodcastDownloader {
    private PodcastItem itemToDownload;

    public void assertIsDownloading(PodcastItem expected) {
        Assert.assertEquals("Incorrect podcast scheduled for downloading",
                expected, itemToDownload);
    }

    @Override
    public void downloadPodcast(PodcastItem item) {
        itemToDownload = item;
    }
}
