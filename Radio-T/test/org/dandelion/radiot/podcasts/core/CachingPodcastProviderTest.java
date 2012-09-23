package org.dandelion.radiot.podcasts.core;

import org.junit.Test;

import java.util.List;

import static org.hamcrest.CoreMatchers.equalTo;
import static org.junit.Assert.assertThat;
import static org.mockito.Mockito.*;

public class CachingPodcastProviderTest {
    private final PodcastsProvider realProvider = mock(PodcastsProvider.class);
    private final PodcastsCache cache = mock(PodcastsCache.class);
    private final CachingPodcastProvider cachedProvider =
            new CachingPodcastProvider(realProvider, cache);
    private final List<PodcastItem> list = new PodcastList();

    @Test
    public void retrievesListFromProviderIfCacheIsInvalid() throws Exception {
        when(cache.isValid()).thenReturn(false);
        when(realProvider.retrieveAll()).thenReturn(list);

        assertThat(cachedProvider.retrieveAll(), equalTo(list));
        verify(realProvider).retrieveAll();
    }

    @Test
    public void updatesCacheWithNewDataIfCacheIsInvalid() throws Exception {
        when(cache.isValid()).thenReturn(false);
        when(realProvider.retrieveAll()).thenReturn(list);

        cachedProvider.retrieveAll();
        verify(cache).updateWith(list);

    }


    @Test
    public void retrievesDataFromCacheIfItHoldsValidData() throws Exception {
        when(cache.isValid()).thenReturn(true);
        when(cache.getData()).thenReturn(list);

        assertThat(cachedProvider.retrieveAll(), equalTo(list));
        verify(realProvider, never()).retrieveAll();
    }
}
