package org.dandelion.radiot.test;

import org.dandelion.radiot.PodcastItem;
import org.dandelion.radiot.PodcastListActivity;
import org.dandelion.radiot.SamplePodcastProvider;

import android.content.Intent;
import android.test.ActivityUnitTestCase;
import android.test.UiThreadTest;
import android.view.View;
import android.widget.ListView;
import android.widget.TextView;

public class PodcastListActivityTestCase extends
		ActivityUnitTestCase<PodcastListActivity> {

	private PodcastListActivity activity;
	private SamplePodcastProvider podcastProvider;

	public PodcastListActivityTestCase() {
		super(PodcastListActivity.class);
	}

	@Override
	protected void setUp() throws Exception {
		super.setUp();
		podcastProvider = new SamplePodcastProvider();
		PodcastListActivity.usePodcastProvider(podcastProvider);
		activity = startActivity(new Intent(), null, null);
	}

	@UiThreadTest
	public void testDisplayPodcastList() throws Exception {
		setPodcastList(new PodcastItem[] { 
				new PodcastItem(121),
				new PodcastItem(122)
		});

		assertEquals(2, getListView().getCount());
	}

	@UiThreadTest
	public void testDisplayPodcastItem() throws Exception {
		View listItem = setupOneItemList(new PodcastItem(121, "19.06.2010",
				"Show notes"));

		assertEquals("#121", getTextOfElement(listItem,
				org.dandelion.radiot.R.id.podcast_item_view_number));
		assertEquals("19.06.2010", getTextOfElement(listItem,
				org.dandelion.radiot.R.id.podcast_item_view_date));
		assertEquals("Show notes", getTextOfElement(listItem,
				org.dandelion.radiot.R.id.podcast_item_view_shownotes));
	}

	@UiThreadTest
	public void testStartPlayActivityOnClick() throws Exception {
		View listItem = setupOneItemList(new PodcastItem(121, "19.06.2010",
				"Show notes", "http://link"));

		clickOnItem(listItem);
		Intent intent = getStartedActivityIntent();

		assertEquals("audio/mpeg", intent.getType());
		assertEquals("http://link", intent.getDataString());
		assertEquals(Intent.ACTION_VIEW, intent.getAction());
	}

	private void clickOnItem(View listItem) {
		getListView().performItemClick(listItem, 0, 0);
	}

	private View setupOneItemList(PodcastItem item) {
		setPodcastList(new PodcastItem[] { item });
		return getListView().getAdapter().getView(0, null, null);
	}

	private void setPodcastList(PodcastItem[] items) {
		podcastProvider.setPodcastList(items);
		activity.refreshPodcasts();
	}

	private String getTextOfElement(View view, int elementId) {
		TextView textView = (TextView) view.findViewById(elementId);
		return textView.getText().toString();
	}

	private ListView getListView() {
		return activity.getListView();
	}
}
