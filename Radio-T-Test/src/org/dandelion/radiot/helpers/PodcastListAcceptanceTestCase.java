package org.dandelion.radiot.helpers;

import android.content.res.AssetManager;
import android.test.ActivityInstrumentationTestCase2;
import org.dandelion.radiot.RadiotApplication;
import org.dandelion.radiot.accepttest.drivers.AppNavigator;
import org.dandelion.radiot.home_screen.HomeScreenActivity;
import org.dandelion.radiot.podcasts.core.PodcastsProvider;
import org.dandelion.radiot.podcasts.core.RssFeedProvider;
import org.dandelion.radiot.podcasts.core.ThumbnailProvider;

import java.io.IOException;
import java.io.InputStream;
import java.util.ArrayList;

public class PodcastListAcceptanceTestCase extends
        ActivityInstrumentationTestCase2<HomeScreenActivity> {
	protected ArrayList<TestLoader> loaders;

    public PodcastListAcceptanceTestCase() {
        super(HomeScreenActivity.class);
    }

    @Override
	protected void setUp() throws Exception {
		loaders = new ArrayList<TestLoader>();
		super.setUp();
	}

    protected TestLoader createLoader(String url) {
        TestLoader loader = new TestLoader(createTestProvider(url));
        loaders.add(loader);
        return loader;
    }

    @Override
	protected void tearDown() throws Exception {
		super.tearDown();
		for (TestLoader loader : loaders) {
			loader.assertNoTasksAreActive();
		}
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