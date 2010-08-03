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
		return new ApplicationDriver(getInstrumentation(), getActivity());
	}

	private void resetApplicationState() {
		RadiotApplication app = (RadiotApplication) getActivity().getApplication();
		app.resetEngines();
	}

	protected IPodcastListEngine createTestPresenter(IModel model) {
		return null;
	}

	protected IModel createTestModel(String url) {
		return null;
	}

	@Override
	protected void tearDown() throws Exception {
		resetApplicationState();
		PodcastList.resetFactory();
		super.tearDown();
	}
}