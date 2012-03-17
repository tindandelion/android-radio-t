package org.dandelion.radiot.podcasts.download;

import org.junit.Test;

import java.io.File;

import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.verify;

public class LocalPodcastProcessorTests {
    @Test
    public void invokesMediaScanner() {
        MediaScanner scanner = mock(MediaScanner.class);
        LocalPodcastProcessor processor = new LocalPodcastProcessor(scanner);
        DownloadTask task = new DownloadTask("Title", new File("/mnt/downloads/local.mp3"));

        processor.downloadComplete(task);

        verify(scanner).scanFile(task.localPath);
    }
}
