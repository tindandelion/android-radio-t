package org.dandelion.radiot.podcasts.core;

import org.dandelion.radiot.podcasts.loader.CachingPodcastLoader;
import org.dandelion.radiot.podcasts.loader.PodcastsCache;
import org.dandelion.radiot.podcasts.loader.PodcastsConsumer;
import org.dandelion.radiot.podcasts.loader.PodcastsProvider;
import org.junit.Before;
import org.junit.Test;

import static org.mockito.Mockito.*;

public class CachingPodcastLoaderTest {
    private final PodcastList cachedList = mock(PodcastList.class, "Cached list");
    private final PodcastList remoteList = mock(PodcastList.class, "Remote list");

    private final PodcastsProvider provider = mock(PodcastsProvider.class);
    private final PodcastsCache cache = mock(PodcastsCache.class);
    private final CachingPodcastLoader loader = new CachingPodcastLoader(provider, cache);
    private final PodcastsConsumer consumer = mock(PodcastsConsumer.class);

    @Test
    public void whenCacheHasNoData_ShouldRetrieveListFromRealProvider() throws Exception {
        cacheHasNoData();

        loader.retrieveTo(consumer);

        verify(consumer).updateList(remoteList);
    }


    @Test
    public void whenTheCacheHasNoData_ShouldUpdateItWithNewData() throws Exception {
        cacheHasNoData();

        loader.retrieveTo(consumer);
        verify(cache).updateWith(remoteList);
    }

    @Test
    public void whenCacheHasFreshData_RetrieveDataFromCache() throws Exception {
        cacheHasFreshData();

        loader.retrieveTo(consumer);
        verify(consumer).updateList(cachedList);
    }

    @Test
    public void whenCacheHasExpiredData_ShouldPopulateDataFromCacheFirstAndThenFromServer() throws Exception {
        cacheHasExpiredData();

        loader.retrieveTo(consumer);
        verify(consumer).updateList(cachedList);
        verify(consumer).updateList(remoteList);
    }

    @Test
    public void whenCacheHasExpiredData_ShouldRefreshItFromServer() throws Exception {
        cacheHasExpiredData();

        loader.retrieveTo(consumer);
        verify(cache).updateWith(remoteList);
    }

    @Before
    public void setUp() throws Exception {
        when(provider.retrieve()).thenReturn(remoteList);
    }

    private void cacheHasNoData() {
        when(cache.hasData()).thenReturn(false);
    }

    private void cacheHasFreshData() {
        when(cache.hasData()).thenReturn(true);
        when(cache.hasExpired()).thenReturn(false);
        when(cache.getData()).thenReturn(cachedList);
    }

    private void cacheHasExpiredData() {
        when(cache.hasData()).thenReturn(true);
        when(cache.hasExpired()).thenReturn(true);
        when(cache.getData()).thenReturn(cachedList);
    }
}
