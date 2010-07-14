package org.dandelion.radiot.test;

import org.dandelion.radiot.HomeScreen;
import org.dandelion.radiot.test.helpers.ApplicationDriver;

import android.test.ActivityInstrumentationTestCase2;

public class HomeScreenTestCase extends
		ActivityInstrumentationTestCase2<HomeScreen> {

	private ApplicationDriver appDriver;

	public HomeScreenTestCase() {
		super("org.dandelion.radiot", HomeScreen.class);
	}

	@Override
	protected void setUp() throws Exception {
		super.setUp();
		LocalRssFeedFactory.install(getInstrumentation());
		appDriver = new ApplicationDriver(getInstrumentation(), getActivity());
	}
	
	@Override
	protected void tearDown() throws Exception {
		LocalRssFeedFactory.uninstall();
		super.tearDown();
	}

	public void testOpenPodcastsPage() throws Exception {
		appDriver.visitMainShowPage();
		assertTrue("The sample podcast record for main podcast show is not found",
				appDriver.waitForText("#5192"));
	}

	public void testShowAfterShowPage() throws Exception {
		appDriver.visitAfterShowPage();
		assertTrue("The sample podcast record for pirates is not found",
				appDriver.waitForText("#10193"));
	}
	
	public void testShowingCorrectActivityTitle() throws Exception {
		appDriver.visitMainShowPage();
		assertEquals("Подкасты", appDriver.getCurrentActivity().getTitle());
	}
	
	public void testShowingOnAirScreen() throws Exception {
		appDriver.visitLiveShowPage();
	}
}
