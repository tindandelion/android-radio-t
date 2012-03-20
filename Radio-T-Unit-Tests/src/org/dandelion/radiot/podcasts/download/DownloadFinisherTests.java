package org.dandelion.radiot.podcasts.download;

import org.junit.Before;
import org.junit.Test;

import java.io.File;

import static org.mockito.Mockito.*;

public class DownloadFinisherTests {
    private MediaScanner scanner;
    private DownloadFinisher finisher;
    private DownloadTask task;
    private Downloader downloader;

    @Before
    public void setUp() throws Exception {
        scanner = mock(MediaScanner.class);
        downloader = mock(Downloader.class);
        finisher = new DownloadFinisher(downloader, scanner);
        task = new DownloadTask()
                .setId(1)
                .setLocalPath(new File("/mnt/downloads"))
                .setSuccessful(true);
    }

    @Test
    public void queriesTaskFromDownloadManagerAndPassesItFurther() throws Exception {
        when(downloader.query(1)).thenReturn(task);
        finisher.finishDownload(1);
        verify(scanner).scanAudioFile(task.localPath);
    }

    @Test
    public void skipsCancelledTasks() throws Exception {
        when(downloader.query(1)).thenReturn(null);
        finisher.finishDownload(1);
        verifyZeroInteractions(scanner);
    }

    @Test
    public void skipsFailedTasks() throws Exception {
        task.setSuccessful(false);
        when(downloader.query(1)).thenReturn(task);
        finisher.finishDownload(1);
        verifyZeroInteractions(scanner);
    }
}
