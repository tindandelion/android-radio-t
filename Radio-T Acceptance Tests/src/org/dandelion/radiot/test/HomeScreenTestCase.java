package org.dandelion.radiot.test;

import org.dandelion.radiot.HomeScreen;
import org.dandelion.radiot.PodcastListActivity;

import android.test.ActivityInstrumentationTestCase2;

import com.jayway.android.robotium.solo.Solo;

public class HomeScreenTestCase extends
		ActivityInstrumentationTestCase2<HomeScreen> {

	private Solo solo;

	public HomeScreenTestCase() {
		super("org.dandelion.radiot", HomeScreen.class);
	}

	@Override
	protected void setUp() throws Exception {
		super.setUp();
		LocalRssFeedFactory.install(getInstrumentation());
		solo = new Solo(getInstrumentation(), getActivity());
	}
	
	@Override
	protected void tearDown() throws Exception {
		LocalRssFeedFactory.uninstall();
		super.tearDown();
	}

	public void testOpenPodcastsPage() throws Exception {
		solo.clickOnText("Подкасты");
		assertTrue("The sample podcast record for main podcast show is not found",
				solo.waitForText("#5192"));
	}

	public void testShowAfterShowPage() throws Exception {
		solo.clickOnText("После-шоу");
		assertTrue("The sample podcast record for pirates is not found",
				solo.waitForText("#10193"));
	}
	
	public void testShowingCorrectActivityTitle() throws Exception {
		solo.clickOnText("Подкасты");
		
		waitForPodcastListToOpen();
		
		assertEquals("Подкасты", solo.getCurrentActivity().getTitle());
	}

	private void waitForPodcastListToOpen() {
		solo.assertCurrentActivity("Failed to open the podcast list", PodcastListActivity.class);
	}
}
