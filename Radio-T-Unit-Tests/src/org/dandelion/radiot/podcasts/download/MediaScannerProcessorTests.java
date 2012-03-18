package org.dandelion.radiot.podcasts.download;

import org.junit.Before;
import org.junit.Test;

import java.io.File;

import static org.mockito.Mockito.*;

public class MediaScannerProcessorTests {
    private MediaScanner scanner;
    private MediaScannerProcessor processor;
    private DownloadTask task;
    private File localPath;

    @Before
    public void setUp() throws Exception {
        scanner = mock(MediaScanner.class);
        processor = new MediaScannerProcessor(scanner);
        localPath = mock(File.class);
        task = new DownloadTask()
                .setLocalPath(localPath);
    }

    @Test
    public void invokesMediaScanner() {
        when(localPath.exists()).thenReturn(true);
        processor.acceptTask(task);
        verify(scanner).scanAudioFile(task.localPath);
    }

    @Test
    public void skipsProcessingIfLocalFileDoesntExist() throws Exception {
        when(localPath.exists()).thenReturn(false);
        processor.acceptTask(task);
        verifyZeroInteractions(scanner);
    }
}
