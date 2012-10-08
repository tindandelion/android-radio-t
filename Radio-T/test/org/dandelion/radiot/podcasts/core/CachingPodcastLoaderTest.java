package org.dandelion.radiot.podcasts.core;

import org.junit.Test;

import static org.hamcrest.CoreMatchers.equalTo;
import static org.junit.Assert.assertThat;
import static org.mockito.Mockito.*;

public class CachingPodcastLoaderTest {
    private final PodcastsProvider realProvider = mock(PodcastsProvider.class);
    private final PodcastsCache cache = mock(PodcastsCache.class);
    private final CachingPodcastLoader cachedLoader =
            new CachingPodcastLoader(realProvider, cache);
    private final PodcastList list = new PodcastList();

    @Test
    public void retrievesListFromProviderIfCacheIsInvalid() throws Exception {
        when(cache.isValid()).thenReturn(false);
        when(realProvider.retrieve()).thenReturn(list);

        assertThat(cachedLoader.retrieve(), equalTo(list));
        verify(realProvider).retrieve();
    }

    @Test
    public void updatesCacheWithNewDataIfCacheIsInvalid() throws Exception {
        when(cache.isValid()).thenReturn(false);
        when(realProvider.retrieve()).thenReturn(list);

        cachedLoader.retrieve();
        verify(cache).updateWith(list);

    }


    @Test
    public void retrievesDataFromCacheIfItHoldsValidData() throws Exception {
        when(cache.isValid()).thenReturn(true);
        when(cache.getData()).thenReturn(list);

        assertThat(cachedLoader.retrieve(), equalTo(list));
        verify(realProvider, never()).retrieve();
    }
}
