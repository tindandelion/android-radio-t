package org.dandelion.radiot.helpers;


import android.content.res.AssetManager;
import android.test.ActivityInstrumentationTestCase2;
import org.dandelion.radiot.RadiotApplication;
import org.dandelion.radiot.accepttest.drivers.HomeScreenDriver;
import org.dandelion.radiot.home_screen.HomeScreenActivity;
import org.dandelion.radiot.podcasts.core.PodcastsProvider;
import org.dandelion.radiot.podcasts.core.RssFeedProvider;
import org.dandelion.radiot.podcasts.core.ThumbnailProvider;

import java.io.IOException;
import java.io.InputStream;

public class BasicAcceptanceTestCase extends
		ActivityInstrumentationTestCase2<HomeScreenActivity> {

	public BasicAcceptanceTestCase() {
		super(HomeScreenActivity.class);
	}

	protected HomeScreenDriver createDriver() {
		configurePodcastEngines();
		return new HomeScreenDriver(getInstrumentation(), getActivity());
	}

	private void configurePodcastEngines() {
		RadiotApplication app = getRadiotApplication();
        app.setPodcastEngine("main-show", createLoader("radio-t"));
		app.setPodcastEngine("after-show", createLoader("pirate-radio-t"));
	}

    protected TestLoader createLoader(String url) {
        return new TestLoader(createTestProvider(url));
    }

    private RadiotApplication getRadiotApplication() {
		return (RadiotApplication) getActivity().getApplication();
	}

    private PodcastsProvider createTestProvider(final String url) {
        final AssetManager assets = getInstrumentation().getContext()
                .getAssets();
        return new RssFeedProvider(url, ThumbnailProvider.Null) {
            @Override
            protected InputStream openContentStream() throws IOException {
                return assets.open((url + ".xml"));
            }
        };
    }
}