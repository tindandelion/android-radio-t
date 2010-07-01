package org.dandelion.radiot.test;

import java.lang.reflect.Field;
import java.util.ArrayList;
import java.util.Date;

import org.dandelion.radiot.PodcastItem;
import org.dandelion.radiot.PodcastList;
import org.dandelion.radiot.PodcastList.IView;
import org.dandelion.radiot.PodcastListActivity;
import org.dandelion.radiot.PodcastList.IFeedSource;
import org.dandelion.radiot.PodcastList.IModel;
import org.dandelion.radiot.PodcastList.IPresenter;

import android.content.Intent;
import android.test.ActivityUnitTestCase;
import android.test.UiThreadTest;
import android.view.View;
import android.widget.TextView;

public class PodcastListActivityTestCase extends
		ActivityUnitTestCase<PodcastListActivity> {

	// This date is 19.06.2010
	private static final Date SAMPLE_DATE = new Date(110, 05, 19);
	protected String feedSourceUrl;
	private PodcastListActivity activity;

	public PodcastListActivityTestCase() {
		super(PodcastListActivity.class);
	}

	@UiThreadTest
	public void testDisplayPodcastItem() throws Exception {
		ArrayList<PodcastItem> items = new ArrayList<PodcastItem>();
		items.add(new PodcastItem(121, SAMPLE_DATE, "Show notes", ""));
		
		activity = startActivityForFeed("");
		activity.updatePodcasts(items);
		View listItem = activity.getListAdapter().getView(0, null, null);

		assertTextEquals(listItem, "podcast_item_view_number", "#121");
		assertTextEquals(listItem, "podcast_item_view_date", "19.06.2010");
		assertTextEquals(listItem, "podcast_item_view_shownotes", "Show notes");
	}
	
	public void testGetsFeedUrlFromBundleExtra() throws Exception {
		startActivityForFeed("podcast_feed_url");
		assertEquals("podcast_feed_url", feedSourceUrl);
		
	}

	private PodcastListActivity startActivityForFeed(String value) {
		Intent intent = new Intent();
		intent.putExtra(PodcastListActivity.URL_KEY, value);
		return startActivity(intent, null, null);
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

	private void assertTextEquals(View listItem, String elementName, String value)
			throws Exception {
		int id = getIdByName(elementName);
		TextView textView = (TextView) listItem.findViewById(id);
		assertEquals(value, textView.getText());
	}

	private int getIdByName(String elementName) throws NoSuchFieldException,
			IllegalAccessException {
		Class cls = org.dandelion.radiot.R.id.class;
		Field field = cls.getDeclaredField(elementName);
		int i = field.getInt(cls);
		return i;
	}
}
