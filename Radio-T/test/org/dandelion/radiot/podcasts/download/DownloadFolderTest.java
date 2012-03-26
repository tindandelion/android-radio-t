package org.dandelion.radiot.podcasts.download;

import org.junit.Test;

import java.io.File;

import static org.junit.Assert.assertEquals;

public class DownloadFolderTest {
    public static final File LOCAL_DIR = new File("/mnt/downloads");
    private final DownloadFolder folder = new DownloadFolder(LOCAL_DIR);

    @Test
    public void constructsDestPathForUrlUsingItsBasename() throws Exception {
        String url = "http://example.com/subdir/my_file.mp3";
        File localPath = folder.makePathForUrl(url);
        assertEquals(new File(LOCAL_DIR, "my_file.mp3"), localPath);
    }
}
