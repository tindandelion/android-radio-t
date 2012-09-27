package org.dandelion.radiot.helpers;

import android.content.res.AssetManager;
import android.test.ActivityInstrumentationTestCase2;
import org.dandelion.radiot.RadiotApplication;
import org.dandelion.radiot.accepttest.drivers.AppNavigator;
import org.dandelion.radiot.home_screen.HomeScreenActivity;
import org.dandelion.radiot.podcasts.core.AsyncPodcastListLoader;
import org.dandelion.radiot.podcasts.core.PodcastsProvider;
import org.dandelion.radiot.podcasts.core.RssFeedProvider;
import org.dandelion.radiot.podcasts.core.ThumbnailProvider;

import java.io.IOException;
import java.io.InputStream;

public class PodcastListAcceptanceTestCase extends
        ActivityInstrumentationTestCase2<HomeScreenActivity> {

    public PodcastListAcceptanceTestCase() {
        super(HomeScreenActivity.class);
    }

    protected AsyncPodcastListLoader createLoader(String url) {
        return new AsyncPodcastListLoader(createTestProvider(url), new FakeCache());
    }

    protected PodcastsProvider createTestProvider(final String url) {
        final AssetManager assets = getInstrumentation().getContext()
                .getAssets();
        return new RssFeedProvider(url, ThumbnailProvider.Null) {
            @Override
            protected InputStream openContentStream() throws IOException {
                return assets.open((url + ".xml"));
            }
        };
    }

    protected AppNavigator createDriver() {
        configurePodcastEngines();
        return new AppNavigator(getInstrumentation(), getActivity());
    }

    protected void configurePodcastEngines() {
        RadiotApplication app = (RadiotApplication) getActivity().getApplication();
        app.setPodcastEngine("main-show", createLoader("radio-t"));
		app.setPodcastEngine("after-show", createLoader("pirate-radio-t"));
	}
}