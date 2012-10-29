package org.dandelion.radiot.podcasts.core;

import java.util.HashMap;

public class MemoryThumbnailCache implements ThumbnailCache {
    private final HashMap<String, byte[]> data = new HashMap<String, byte[]>();

    @Override
    public void update(String url, byte[] thumbnail) {
        data.put(url, thumbnail);
    }

    @Override
    public byte[] lookup(String url) {
        if (data.containsKey(url)) {
            return data.get(url);
        } else {
            return null;
        }
    }
}
