package org.dandelion.radiot.podcasts.loader;

public class CachingThumbnailProvider implements ThumbnailProvider {
    private ThumbnailProvider provider;
    private ThumbnailCache cache;

    public CachingThumbnailProvider(ThumbnailProvider provider, ThumbnailCache cache) {
        this.provider = provider;
        this.cache = cache;
    }

    @Override
    public byte[] thumbnailDataFor(String url) {
        byte[] data = cache.lookup(url);
        if (data == null) {
            data = provider.thumbnailDataFor(url);
            cache.update(url, data);
        }
        return data;
    }
}
