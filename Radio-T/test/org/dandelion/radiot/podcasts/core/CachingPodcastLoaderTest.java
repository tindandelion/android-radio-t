package org.dandelion.radiot.podcasts.core;

import org.junit.Test;

import static org.mockito.Mockito.*;

public class CachingPodcastLoaderTest {
    private final PodcastsProvider realProvider = mock(PodcastsProvider.class);
    private final PodcastsCache cache = mock(PodcastsCache.class);
    private final CachingPodcastLoader cachedLoader =
            new CachingPodcastLoader(realProvider, cache);
    private final PodcastsConsumer consumer = mock(PodcastsConsumer.class);
    private final PodcastList list = new PodcastList();

    @Test
    public void whenCacheIsInvalid_ShouldRetrieveListFromRealProvider() throws Exception {
        when(cache.isValid()).thenReturn(false);
        when(realProvider.retrieve()).thenReturn(list);

        cachedLoader.retrieveTo(consumer);
        verify(consumer).updatePodcasts(list);
    }

    @Test
    public void whenTheCacheIsInvalid_ShouldUpdateItWithNewData() throws Exception {
        when(cache.isValid()).thenReturn(false);
        when(realProvider.retrieve()).thenReturn(list);

        cachedLoader.retrieveTo(consumer);
        verify(cache).updateWith(list);
    }


    @Test
    public void whenCacheHasValidData_RetrieveDataFromCache() throws Exception {
        when(cache.isValid()).thenReturn(true);
        when(cache.getData()).thenReturn(list);

        cachedLoader.retrieveTo(consumer);
        verify(consumer).updatePodcasts(list);
    }
}
