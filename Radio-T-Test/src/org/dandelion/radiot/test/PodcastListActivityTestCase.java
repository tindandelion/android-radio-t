package org.dandelion.radiot.test;

import java.util.ArrayList;

import org.dandelion.radiot.PodcastItem;
import org.dandelion.radiot.PodcastList;
import org.dandelion.radiot.PodcastList.IModel;
import org.dandelion.radiot.PodcastList.IPresenter;
import org.dandelion.radiot.PodcastList.IView;
import org.dandelion.radiot.PodcastListActivity;

import android.content.Intent;
import android.test.ActivityUnitTestCase;
import android.test.UiThreadTest;

public class PodcastListActivityTestCase extends
		ActivityUnitTestCase<PodcastListActivity> {

	protected String feedSourceUrl;
	private PodcastListActivity activity;

	public PodcastListActivityTestCase() {
		super(PodcastListActivity.class);
	}

	public void testGetsFeedUrlFromBundleExtra() throws Exception {
		Intent intent = new Intent();
		intent.putExtra(PodcastListActivity.URL_KEY, "podcast_feed_url");

		startActivity(intent, null, null);

		assertEquals("podcast_feed_url", feedSourceUrl);

	}

	public void testGetsTitleFromExtra() throws Exception {
		Intent intent = new Intent();
		intent.putExtra(PodcastListActivity.TITLE_KEY, "Custom title");

		activity = startActivity(intent, null, null);

		assertEquals("Custom title", activity.getTitle());
	}
	
	@UiThreadTest
	public void testUpdatingPodcastList() throws Exception {
		activity = startActivity(new Intent(), null, null);
		assertEquals(0, activity.getListView().getCount());
		
		ArrayList<PodcastItem> newList = new ArrayList<PodcastItem>();
		PodcastItem itemToDisplay = new PodcastItem();
		newList.add(itemToDisplay);
		
		activity.updatePodcasts(newList);
		assertEquals(1, activity.getListView().getCount());
		Object displayedItem = activity.getListAdapter().getItem(0);
		
		assertEquals(itemToDisplay, displayedItem);
	}

	protected IPresenter nullPresenter() {
		return new PodcastList.IPresenter() {
			public void refreshData() {
			}
			public void cancelLoading() {
			}
		};
	}

	@Override
	protected void setUp() throws Exception {
		super.setUp();
		PodcastList.setFactory(new PodcastList.Factory() {
			@Override
			public IModel createModel(String url) {
				feedSourceUrl = url;
				return super.createModel(url);
			};

			@Override
			public IPresenter createPresenter(IModel model, IView view) {
				return nullPresenter();
			}
		});
	}

	@Override
	protected void tearDown() throws Exception {
		PodcastList.resetFactory();
		super.tearDown();
	}
}
