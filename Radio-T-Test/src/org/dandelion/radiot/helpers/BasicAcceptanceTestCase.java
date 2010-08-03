package org.dandelion.radiot.helpers;

import org.dandelion.radiot.HomeScreen;
import org.dandelion.radiot.PodcastList;
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
		PodcastList.setFactory(new PodcastList.Factory() {
			public IModel createModel(String url) {
				return createTestModel(url);
			};

			public IPodcastListEngine createPresenter(IModel model) {
				return createTestPresenter(model);
			};
		});
		configurePodcastEngines();
		return new ApplicationDriver(getInstrumentation(), getActivity());
	}

	private void configurePodcastEngines() {
		RadiotApplication app = (RadiotApplication) getActivity().getApplication();
		app.setPodcastEngine("main-show", createTestPresenter(createTestModel("radio-t")));
		app.setPodcastEngine("after-show", createTestPresenter(createTestModel("pirate-radio-t")));
	}

	protected IPodcastListEngine createTestPresenter(IModel model) {
		return null;
	}

	protected IModel createTestModel(String url) {
		return null;
	}

	@Override
	protected void tearDown() throws Exception {
		PodcastList.resetFactory();
		super.tearDown();
	}
}