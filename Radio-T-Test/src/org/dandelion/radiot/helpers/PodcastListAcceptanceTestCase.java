package org.dandelion.radiot.helpers;

import java.util.ArrayList;

public class PodcastListAcceptanceTestCase extends BasicAcceptanceTestCase {
	protected ArrayList<TestLoader> loaders;

    @Override
    protected TestLoader createLoader(String url) {
        TestLoader loader = super.createLoader(url);
        loaders.add(loader);
        return loader;
    }

	public TestLoader mainShowPresenter() {
		return loaders.get(0);
	}
	
	public TestLoader afterShowPresenter() {
		return loaders.get(1);
	}
	
	@Override
	protected void setUp() throws Exception {
		loaders = new ArrayList<TestLoader>();
		super.setUp();
	}

	@Override
	protected void tearDown() throws Exception {
		super.tearDown();
		for (TestLoader loader : loaders) {
			loader.assertNoTasksAreActive();
		}
	}

}