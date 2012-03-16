package org.dandelion.radiot.podcasts.download;

import org.dandelion.radiot.podcasts.core.PodcastItem;
import org.junit.Before;
import org.junit.Test;

import java.io.File;

import static org.mockito.Matchers.anyString;
import static org.mockito.Matchers.eq;
import static org.mockito.Mockito.*;

@SuppressWarnings("ResultOfMethodCallIgnored")
public class PodcastDownloaderTests {
    public static final String SOURCE_FILENAME = "rt_podcast_1.mp3";
    public static final String SOURCE_URL = "http://radio-t.com/" + SOURCE_FILENAME;

    private PodcastDownloadManager manager;
    private RealPodcastDownloader downloader;
    private PodcastItem item;
    private DownloadFolder downloadFolder;

    @Before
    public void setUp() throws Exception {
        downloadFolder = mock(DownloadFolder.class);
        manager = mock(PodcastDownloadManager.class);
        downloader = new RealPodcastDownloader(manager, downloadFolder);
        item = new PodcastItem();
        item.setAudioUri(SOURCE_URL);
    }

    @Test
    public void providesPodcastUri() throws Exception {
        downloader.process(null, SOURCE_URL);
        verify(manager).submitRequest(eq(SOURCE_URL), any(File.class));
    }

    @Test
    public void downloadsPodcastIntoDownloadFolder() throws Exception {
        File destPath = new File("/mnt/download/filename.mp3");
        when(downloadFolder.makePathForUrl(SOURCE_URL))
                .thenReturn(destPath);
        downloader.process(null, SOURCE_URL);
        verify(manager).submitRequest(anyString(), eq(destPath));
    }

    @Test
    public void ensureDestinationFolderExists() throws Exception {
        downloader.process(null, SOURCE_URL);
        verify(downloadFolder).ensureExists();
    }
}
