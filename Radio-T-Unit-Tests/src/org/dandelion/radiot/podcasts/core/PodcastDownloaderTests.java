package org.dandelion.radiot.podcasts.core;

import org.junit.Before;
import org.junit.Test;

import java.io.File;

import static org.mockito.Matchers.anyString;
import static org.mockito.Matchers.eq;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

@SuppressWarnings("ResultOfMethodCallIgnored")
public class PodcastDownloaderTests {
    public static final String SOURCE_FILENAME = "rt_podcast_1.mp3";
    public static final String SOURCE_URL = "http://radio-t.com/" + SOURCE_FILENAME;
    public static final String DEST_FOLDER = "/mnt/downloads/";

    private PodcastDownloadManager manager;
    private PodcastDownloader downloader;
    private PodcastItem item;
    private File destFolder;

    @Before
    public void setUp() throws Exception {
        destFolder = mockDestFolder();
        manager = mock(PodcastDownloadManager.class);
        downloader = new PodcastDownloader(manager, destFolder);
        item = new PodcastItem();
        item.setAudioUri(SOURCE_URL);
    }

    private File mockDestFolder() {
        File f = mock(File.class);
        when(f.toString()).thenReturn(DEST_FOLDER);
        return f;
    }

    @Test
    public void providesPodcastUri() throws Exception {
        downloader.downloadPodcast(item);
        verify(manager).submitRequest(eq(SOURCE_URL), anyString());
    }

    @Test
    public void constructsDestinationPathUsingFolderAndSourceFileName() throws Exception {
        downloader.downloadPodcast(item);
        verify(manager).submitRequest(anyString(), eq(DEST_FOLDER + SOURCE_FILENAME));
    }

    @Test
    public void ensureDestinationFolderExists() throws Exception {
        downloader.downloadPodcast(item);
        verify(destFolder).mkdirs();
    }
}
