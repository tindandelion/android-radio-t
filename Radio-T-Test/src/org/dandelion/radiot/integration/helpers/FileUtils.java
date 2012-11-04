package org.dandelion.radiot.integration.helpers;

import java.io.File;

public class FileUtils {

    @SuppressWarnings("ResultOfMethodCallIgnored")
    public static void deleteDir(File dir) {
        if (!dir.exists()) {
            return;
        }

        File[] files = dir.listFiles();
        for(File f : files) {
            if (f.isFile()) {
                f.delete();
            } else {
                deleteDir(f);
            }
        }
        dir.delete();
    }
}
