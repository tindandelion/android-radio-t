package org.dandelion.radiot.test;


import org.dandelion.radiot.PodcastItem;
import org.dandelion.radiot.PodcastListActivity;
import org.dandelion.radiot.test.helpers.ApplicationDriver;
import org.dandelion.radiot.test.helpers.FakePodcastPlayer;
import org.dandelion.radiot.test.helpers.PodcastListAcceptanceTestCase;

import android.test.UiThreadTest;
import android.view.View;

public class PlayingPodcastsFromList extends PodcastListAcceptanceTestCase {
	private PodcastListActivity activity;
	private ApplicationDriver appDriver;
	private FakePodcastPlayer player;

	@Override
	protected void setUp() throws Exception {
		super.setUp();
		appDriver = createApplicationDriver();
		activity = appDriver.visitMainShowPage();
		player = new FakePodcastPlayer();
		activity.setPodcastPlayer(player);
		testPresenter.assertPodcastListIsUpdated();
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
