package org.dandelion.radiot.accepttest;

import org.dandelion.radiot.accepttest.drivers.HomeScreenDriver;
import org.dandelion.radiot.helpers.PodcastListAcceptanceTestCase;

public class HomeScreenTestCase extends
		PodcastListAcceptanceTestCase {
	
	private HomeScreenDriver driver;

	@Override
	protected void setUp() throws Exception {
		super.setUp();
		driver = createDriver();
	}

	public void testOpenPodcastsPage() throws Exception {
		driver.visitMainShowPage2();
		mainShowPresenter().assertPodcastListIsUpdated();
		assertTrue("The sample podcast record for main podcast show is not found",
                driver.waitForText("#5192"));
	}

	public void testShowAfterShowPage() throws Exception {
		driver.visitAfterShowPage();
		afterShowPresenter().assertPodcastListIsUpdated();
		assertTrue("The sample podcast record for pirates is not found",
                driver.waitForText("#10193"));
	}
	
	public void testShowingCorrectActivityTitle() throws Exception {
		driver.visitMainShowPage2();
		mainShowPresenter().assertPodcastListIsUpdated();
		assertEquals("Подкасты", driver.getCurrentActivity().getTitle());
	}
}
