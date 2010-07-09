package org.dandelion.radiot.test;

import org.dandelion.radiot.PodcastList;
import org.dandelion.radiot.PodcastList.IFeedSource;
import org.dandelion.radiot.PodcastList.IModel;
import org.dandelion.radiot.PodcastList.IPresenter;
import org.dandelion.radiot.PodcastList.IView;
import org.dandelion.radiot.PodcastListActivity;

import android.content.Intent;
import android.test.ActivityUnitTestCase;

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

	protected IPresenter nullPresenter() {
		return new PodcastList.IPresenter() {
			public void refreshData() {
			}
		};
	}

	@Override
	protected void setUp() throws Exception {
		super.setUp();
		PodcastList.setFactory(new PodcastList.Factory() {
			@Override
			public IFeedSource createFeedSource(String url) {
				feedSourceUrl = url;
				return super.createFeedSource(url);
			}

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
