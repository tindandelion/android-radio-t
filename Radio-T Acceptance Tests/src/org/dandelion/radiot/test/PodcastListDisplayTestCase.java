package org.dandelion.radiot.test;

import org.dandelion.radiot.PodcastListActivity;

import android.test.ActivityInstrumentationTestCase2;

import com.jayway.android.robotium.solo.Solo;

public class PodcastListDisplayTestCase extends
		ActivityInstrumentationTestCase2<PodcastListActivity> {

	private Solo solo;

	public PodcastListDisplayTestCase() {
		super("org.dandelion.radiot", PodcastListActivity.class);
	}
	
	@Override
	protected void setUp() throws Exception {
		super.setUp();
		solo = new Solo(getInstrumentation(), getActivity());
	}
	
	public void testPreConditions() throws Exception {
		assertNotNull(solo);
		solo.assertCurrentActivity("Current Activity", PodcastListActivity.class);
		
		solo.waitForDialogToClose(20000);
	}
}
