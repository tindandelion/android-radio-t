package org.dandelion.radiot.test;

import org.dandelion.radiot.PodcastItem;
import org.dandelion.radiot.PodcastListActivity;

import android.test.ActivityInstrumentationTestCase2;
import android.test.UiThreadTest;
import android.view.MotionEvent;
import android.view.View;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.TextView;

public class PodcastListDisplayTest extends
		ActivityInstrumentationTestCase2<PodcastListActivity> {

	private PodcastListActivity activity;

	public PodcastListDisplayTest() {
		super("org.dandelion.radiot", PodcastListActivity.class);
	}

	@Override
	protected void setUp() throws Exception {
		super.setUp();
		activity = getActivity();
	}

	@UiThreadTest
	public void testDisplayPodcastList() throws Exception {
		setPodcastListItems(new String[] { "#121", "#122" });

		assertEquals(2, getListView().getCount());
	}

	@UiThreadTest
	public void testDisplayPodcastItem() throws Exception {
		View listItem = setupOneItemList(new PodcastItem("#121",
				"19.06.2010", "Show notes"));

		assertEquals("#121", getTextOfElement(listItem,
				org.dandelion.radiot.R.id.podcast_item_view_number));
		assertEquals("19.06.2010", getTextOfElement(listItem,
				org.dandelion.radiot.R.id.podcast_item_view_date));
		assertEquals("Show notes", getTextOfElement(listItem,
				org.dandelion.radiot.R.id.podcast_item_view_shownotes));
	}

	@UiThreadTest
	public void testPlayingPodcastOnClick() throws Exception {
		View listItem = setupOneItemList(new PodcastItem("#121",
				"19.06.2010", "Show notes", "http://link"));
		
		listItem.performClick();
		
		assertOpenedMediaFile("http://link");
	}

	private void assertOpenedMediaFile(String link) {
		fail();
	}

	private View setupOneItemList(PodcastItem item) {
		activity.setPodcastList(new PodcastItem[] { item });
		return getListView().getAdapter().getView(0, null, null);
	}

	private String getTextOfElement(View view, int elementId) {
		TextView textView = (TextView) view.findViewById(elementId);
		return textView.getText().toString();
	}

	private ListView getListView() {
		return activity.getListView();
	}

	private void setPodcastListItems(String[] items) {
		PodcastItem podcastItems[] = new PodcastItem[items.length];
		for (int i = 0; i < items.length; i++) {
			podcastItems[i] = new PodcastItem(items[i]);
		}
		activity.setPodcastList(podcastItems);
	}

}
