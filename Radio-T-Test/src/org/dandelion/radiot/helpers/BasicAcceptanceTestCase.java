package org.dandelion.radiot.helpers;

import org.dandelion.radiot.HomeScreen;
import org.dandelion.radiot.PodcastList.IModel;
import org.dandelion.radiot.PodcastList.IPodcastListEngine;
import org.dandelion.radiot.RadiotApplication;

import android.test.ActivityInstrumentationTestCase2;

public class BasicAcceptanceTestCase extends
		ActivityInstrumentationTestCase2<HomeScreen> {

	public BasicAcceptanceTestCase() {
		super("org.dandelion.radiot", HomeScreen.class);
	}

	protected ApplicationDriver createApplicationDriver() {
		configurePodcastEngines();
		return new ApplicationDriver(getInstrumentation(), getActivity());
	}

	private void configurePodcastEngines() {
		RadiotApplication app = (RadiotApplication) getActivity().getApplication();
		app.setPodcastEngine("main-show", createTestEngine(createTestModel("radio-t")));
		app.setPodcastEngine("after-show", createTestEngine(createTestModel("pirate-radio-t")));
	}

	protected IPodcastListEngine createTestEngine(IModel model) {
		return null;
	}

	protected IModel createTestModel(String url) {
		return null;
	}

	@Override
	protected void tearDown() throws Exception {
		super.tearDown();
	}
}