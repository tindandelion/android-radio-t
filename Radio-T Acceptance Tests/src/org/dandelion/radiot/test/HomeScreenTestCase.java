package org.dandelion.radiot.test;

import org.dandelion.radiot.test.helpers.ApplicationDriver;
import org.dandelion.radiot.test.helpers.BasicAcceptanceTestCase;


public class HomeScreenTestCase extends
		BasicAcceptanceTestCase {
	
	private ApplicationDriver appDriver;

	@Override
	protected void setUp() throws Exception {
		super.setUp();
		appDriver = createApplicationDriver();
	}

	protected void tweakPodcastListFactory() {
		LocalRssFeedFactory.install(getInstrumentation());
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
