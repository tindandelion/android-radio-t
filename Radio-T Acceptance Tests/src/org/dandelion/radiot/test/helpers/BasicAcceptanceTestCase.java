package org.dandelion.radiot.test.helpers;

import org.dandelion.radiot.HomeScreen;
import org.dandelion.radiot.PodcastList;

import android.test.ActivityInstrumentationTestCase2;

public class BasicAcceptanceTestCase extends
		ActivityInstrumentationTestCase2<HomeScreen> {

	public BasicAcceptanceTestCase() {
		super("org.dandelion.radiot", HomeScreen.class);
	}

	protected ApplicationDriver createApplicationDriver() {
		tweakPodcastListFactory();
		return new ApplicationDriver(getInstrumentation(), getActivity());
	}

	protected void tweakPodcastListFactory() {
	}

	@Override
	protected void tearDown() throws Exception {
		PodcastList.resetFactory();
		super.tearDown();
	}
}