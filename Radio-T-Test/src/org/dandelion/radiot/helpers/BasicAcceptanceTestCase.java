package org.dandelion.radiot.helpers;


import org.dandelion.radiot.accepttest.drivers.HomeScreenDriver;
import org.dandelion.radiot.podcasts.core.PodcastListLoader;
import org.dandelion.radiot.home_screen.HomeScreenActivity;
import org.dandelion.radiot.RadiotApplication;

import android.test.ActivityInstrumentationTestCase2;
import org.dandelion.radiot.podcasts.core.PodcastsProvider;

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
		app.setPodcastEngine("main-show", createTestEngine(createTestModel("radio-t")));
		app.setPodcastEngine("after-show", createTestEngine(createTestModel("pirate-radio-t")));
	}

	protected RadiotApplication getRadiotApplication() {
		return (RadiotApplication) getActivity().getApplication();
	}

	protected PodcastListLoader createTestEngine(PodcastsProvider model) {
		return null;
	}

	protected PodcastsProvider createTestModel(String url) {
		return null;
	}

	@Override
	protected void tearDown() throws Exception {
		super.tearDown();
	}
}