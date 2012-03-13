package org.dandelion.radiot.helpers;

import junit.framework.Assert;
import org.dandelion.radiot.podcasts.core.PodcastDownloader;
import org.dandelion.radiot.podcasts.core.PodcastItem;

public class FakePodcastDownloader implements PodcastDownloader {
    private SyncValueHolder<PodcastItem> itemToDownload = new SyncValueHolder<PodcastItem>();

    public void assertIsDownloading(PodcastItem expected) throws InterruptedException {
        Assert.assertEquals("Incorrect podcast scheduled for downloading",
                expected, itemToDownload.getValue());
    }

    @Override
    public void downloadPodcast(PodcastItem item) {
        itemToDownload.setValue(item);
    }
}
