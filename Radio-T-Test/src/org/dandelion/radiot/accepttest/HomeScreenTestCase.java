package org.dandelion.radiot.accepttest;

import org.dandelion.radiot.helpers.ApplicationDriver;
import org.dandelion.radiot.helpers.PodcastListAcceptanceTestCase;


public class HomeScreenTestCase extends
		PodcastListAcceptanceTestCase {
	
	private ApplicationDriver appDriver;

	@Override
	protected void setUp() throws Exception {
		super.setUp();
		appDriver = createApplicationDriver();
	}

	public void testOpenPodcastsPage() throws Exception {
		appDriver.visitMainShowPage();
		testPresenter.assertPodcastListIsUpdated();
		assertTrue("The sample podcast record for main podcast show is not found",
				appDriver.waitForText("#5192"));
	}

	public void testShowAfterShowPage() throws Exception {
		appDriver.visitAfterShowPage();
		testPresenter.assertPodcastListIsUpdated();
		assertTrue("The sample podcast record for pirates is not found",
				appDriver.waitForText("#10193"));
	}
	
	public void testShowingCorrectActivityTitle() throws Exception {
		appDriver.visitMainShowPage();
		testPresenter.assertPodcastListIsUpdated();
		assertEquals("Подкасты", appDriver.getCurrentActivity().getTitle());
	}
	
	public void testShowingOnAirScreen() throws Exception {
		appDriver.visitLiveShowPage();
	}
}
