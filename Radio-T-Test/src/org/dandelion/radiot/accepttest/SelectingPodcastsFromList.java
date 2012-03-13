package org.dandelion.radiot.accepttest;


import org.dandelion.radiot.accepttest.drivers.ApplicationDriver;
import org.dandelion.radiot.accepttest.drivers.PodcastListDriver;
import org.dandelion.radiot.helpers.FakePodcastPlayer;
import org.dandelion.radiot.helpers.PodcastListAcceptanceTestCase;
import org.dandelion.radiot.podcasts.PodcastsApp;
import org.dandelion.radiot.podcasts.core.PodcastItem;
import org.dandelion.radiot.podcasts.core.PodcastPlayer;

class TestingPodcastsApp extends PodcastsApp {
    private PodcastPlayer player;

    TestingPodcastsApp(PodcastPlayer player) {
        super();
        this.player = player;
    }

    @Override
    public PodcastPlayer getPodcastPlayer() {
        return player;
    }
}

public class SelectingPodcastsFromList extends PodcastListAcceptanceTestCase {
    private FakePodcastPlayer player;
    private PodcastListDriver listDriver;

    @Override
	protected void setUp() throws Exception {
		super.setUp();
        setupEnvironment();
        ApplicationDriver appDriver = createApplicationDriver();
		listDriver = appDriver.visitMainShowPage2();
		mainShowPresenter().assertPodcastListIsUpdated();
	}

    public void testStartPlayingPodcastOnClick() throws Exception {
        PodcastItem item = listDriver.selectItemForPlaying(0);
		player.assertIsPlaying(item.getAudioUri());
	}

    private void setupEnvironment() {
        player = new FakePodcastPlayer();
        PodcastsApp.setTestingInstance(new TestingPodcastsApp(player));
    }
}
