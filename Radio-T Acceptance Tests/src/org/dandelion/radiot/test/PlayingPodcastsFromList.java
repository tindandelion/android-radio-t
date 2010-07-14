package org.dandelion.radiot.test;


import org.dandelion.radiot.PodcastItem;
import org.dandelion.radiot.PodcastList.Factory;
import org.dandelion.radiot.PodcastListActivity;
import org.dandelion.radiot.test.helpers.ApplicationDriver;
import org.dandelion.radiot.test.helpers.BasicAcceptanceTestCase;
import org.dandelion.radiot.test.helpers.FakePodcastPlayer;

import android.test.UiThreadTest;
import android.view.View;

public class PlayingPodcastsFromList extends BasicAcceptanceTestCase {
	private PodcastListActivity activity;
	private ApplicationDriver appDriver;
	private FakePodcastPlayer player;

	@Override
	protected Factory createPodcastListFactory() {
		return new LocalRssFeedFactory(getInstrumentation());
	}

	@Override
	protected void setUp() throws Exception {
		super.setUp();
		appDriver = createApplicationDriver();
		activity = appDriver.visitMainShowPage();
		player = new FakePodcastPlayer();
		activity.setPodcastPlayer(player);
	}

	@UiThreadTest
	public void testStartPlayingPodcastOnClick() throws Exception {
		PodcastItem item = (PodcastItem) activity.getListAdapter().getItem(0);

		clickOnListItem(0);

		player.assertIsPlaying(item.getAudioUri());
	}

	private void clickOnListItem(int index) {
		View view = activity.getListAdapter().getView(index, null, null);
		activity.getListView().performItemClick(view, index, 0);
	}
}
