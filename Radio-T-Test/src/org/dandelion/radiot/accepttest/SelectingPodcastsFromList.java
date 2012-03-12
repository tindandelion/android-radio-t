package org.dandelion.radiot.accepttest;


import android.widget.ListAdapter;
import org.dandelion.radiot.podcasts.PodcastsApp;
import org.dandelion.radiot.podcasts.core.PodcastItem;
import org.dandelion.radiot.podcasts.core.PodcastPlayer;
import org.dandelion.radiot.podcasts.ui.PodcastListActivity;
import org.dandelion.radiot.helpers.ApplicationDriver;
import org.dandelion.radiot.helpers.FakePodcastPlayer;
import org.dandelion.radiot.helpers.PodcastListAcceptanceTestCase;

import android.test.UiThreadTest;
import android.view.View;

class TestingPodcastsApp extends PodcastsApp {
    private PodcastPlayer player;

    TestingPodcastsApp(PodcastPlayer player) {
        super(null);
        this.player = player;
    }

    @Override
    public PodcastPlayer getPodcastPlayer() {
        return player;
    }
}

public class SelectingPodcastsFromList extends PodcastListAcceptanceTestCase {
	private PodcastListActivity activity;
	private ApplicationDriver appDriver;
	private FakePodcastPlayer player;

	@Override
	protected void setUp() throws Exception {
		super.setUp();
		appDriver = createApplicationDriver();
		activity = appDriver.visitMainShowPage();
		player = new FakePodcastPlayer();
        PodcastsApp.setTestingInstance(new TestingPodcastsApp(player));
		mainShowPresenter().assertPodcastListIsUpdated();
	}

	@UiThreadTest
	public void testStartPlayingPodcastOnClick() throws Exception {
        PodcastItem item = clickOnPodcastItem(0);
		player.assertIsPlaying(item.getAudioUri());
	}

    private PodcastItem clickOnPodcastItem(int index) {
        ListAdapter adapter = activity.getListAdapter();
        View view = adapter.getView(index, null, null);
        PodcastItem item = (PodcastItem) adapter.getItem(index);
        activity.getListView().performItemClick(view, 0, 0);
        return item;
    }
}
