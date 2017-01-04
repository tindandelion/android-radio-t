package org.dandelion.radiot.podcasts.main;

import android.content.Context;
import android.media.MediaScannerConnection;
import org.dandelion.radiot.podcasts.download.MediaScanner;

import java.io.File;

class SystemMediaScanner implements MediaScanner {
    private Context context;

    public SystemMediaScanner(Context context) {
        this.context = context;
    }

    @Override
    public void scanAudioFile(File path) {
        MediaScannerConnection.scanFile(context, 
                new String[] {path.toString()},
                new String[] {"audio/mpeg"},
                null);
    }
}
