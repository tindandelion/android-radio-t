package org.dandelion.radiot.podcasts.core;

import org.dandelion.radiot.podcasts.loader.ThumbnailProvider;
import org.junit.Test;

import static org.hamcrest.CoreMatchers.equalTo;
import static org.junit.Assert.assertThat;
import static org.mockito.Mockito.*;

public class CachingThumbnailProviderTest {
    public static final String URL = "http://example.com/thumbnail.jpg";
    public static final byte[] THUMBNAIL = new byte[0];
    private final ThumbnailProvider realProvider = mock(ThumbnailProvider.class);
    private final ThumbnailCache cache = mock(ThumbnailCache.class);
    private final CachingThumbnailProvider provider = new CachingThumbnailProvider(realProvider, cache);

    @Test
    public void whenRequestedThumbnail_callsRealProvider() throws Exception {
        when(realProvider.thumbnailDataFor(URL)).thenReturn(THUMBNAIL);

        assertThat(provider.thumbnailDataFor(URL), equalTo(THUMBNAIL));
    }

    @Test
    public void whenRequestedThumbnail_savesDataInCache() throws Exception {
        when(realProvider.thumbnailDataFor(URL)).thenReturn(THUMBNAIL);

        provider.thumbnailDataFor(URL);
        verify(cache).update(URL, THUMBNAIL);
    }

    @Test
    public void whenRequestedCachedThumbnail_returnsOneFromCache() throws Exception {
        when(cache.lookup(URL)).thenReturn(THUMBNAIL);

        assertThat(provider.thumbnailDataFor(URL), equalTo(THUMBNAIL));
        verifyZeroInteractions(realProvider);
    }
}
