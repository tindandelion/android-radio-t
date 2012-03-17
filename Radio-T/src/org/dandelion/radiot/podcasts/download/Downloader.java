package org.dandelion.radiot.podcasts.download;

import java.io.File;

public interface Downloader {
    long submitRequest(String src, File dest);
}
