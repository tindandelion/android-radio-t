package org.dandelion.radiot.podcasts.core;

import android.net.Uri;
import org.junit.Before;
import org.junit.Test;

import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.verify;

public class PodcastDownloaderTests {

    private PodcastDownloadManager manager;
    private PodcastDownloader downloader;
    private PodcastItem item;

    @Before
    public void setUp() throws Exception {
        manager = mock(PodcastDownloadManager.class);
        downloader = new PodcastDownloader(manager);
    }

    @Test
    public void submitDownloadRequest() throws Exception {

        Uri source = Uri.parse("http://radio-t.com/rt_podcast_1.mp3");
        item = new PodcastItem();
        item.setAudiUri(source);
        downloader.downloadPodcast(item);
        verify(manager).submitRequest(source, null);
    }
}
