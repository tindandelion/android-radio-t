package org.dandelion.radiot.test;

import org.dandelion.radiot.HomeScreen;
import org.dandelion.radiot.PodcastList;
import org.dandelion.radiot.PodcastList.IFeedSource;
import org.dandelion.radiot.PodcastList.IModel;
import org.dandelion.radiot.PodcastList.IPresenter;
import org.dandelion.radiot.PodcastList.IView;
import org.dandelion.radiot.RssFeedModel.AssetFeedSource;

import android.content.res.AssetManager;
import android.net.Uri;
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
		PodcastList.setFactory(new PodcastList.Factory() {
			
			@Override
			public IFeedSource createFeedSource(String url) {
				AssetManager assets = getInstrumentation().getTargetContext().getAssets();
				return new AssetFeedSource(assets, getLocalFileName(url));
			}
			
			private String getLocalFileName(String url) {
				Uri uri = Uri.parse(url);
				return uri.getLastPathSegment() + ".xml";
			}

			@Override
			public IPresenter createPresenter(IModel model, IView view) {
				return PodcastList.createSyncPresenter(model, view);
			}
		});
		solo = new Solo(getInstrumentation(), getActivity());
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
}
