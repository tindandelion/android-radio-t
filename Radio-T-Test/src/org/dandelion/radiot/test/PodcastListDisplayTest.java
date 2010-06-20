package org.dandelion.radiot.test;

import org.dandelion.radiot.PodcastItem;
import org.dandelion.radiot.PodcastListActivity;
import org.dandelion.radiot.PodcastListActivity.IPodcastPlayer;

import android.net.Uri;
import android.test.ActivityInstrumentationTestCase2;
import android.test.UiThreadTest;
import android.view.KeyEvent;
import android.view.View;
import android.widget.ListView;
import android.widget.TextView;

public class PodcastListDisplayTest extends
		ActivityInstrumentationTestCase2<PodcastListActivity> implements
		IPodcastPlayer {

	private PodcastListActivity activity;
	private Uri playedPodcastUri;

	public PodcastListDisplayTest() {
		super("org.dandelion.radiot", PodcastListActivity.class);
	}

	@Override
	protected void setUp() throws Exception {
		super.setUp();
		setActivityInitialTouchMode(false);
		activity = getActivity();
		activity.setPodcastPlayer(this);
	}

	@UiThreadTest
	public void testDisplayPodcastList() throws Exception {
		setPodcastListItems(new String[] { "#121", "#122" });

		assertEquals(2, getListView().getCount());
	}

	@UiThreadTest
	public void testDisplayPodcastItem() throws Exception {
		View listItem = setupOneItemList(new PodcastItem("#121", "19.06.2010",
				"Show notes"));

		assertEquals("#121", getTextOfElement(listItem,
				org.dandelion.radiot.R.id.podcast_item_view_number));
		assertEquals("19.06.2010", getTextOfElement(listItem,
				org.dandelion.radiot.R.id.podcast_item_view_date));
		assertEquals("Show notes", getTextOfElement(listItem,
				org.dandelion.radiot.R.id.podcast_item_view_shownotes));
	}

	public void testPlayingPodcastOnClick() throws Exception {
		activity.runOnUiThread(new Runnable() {
			public void run() {
				setupOneItemList(new PodcastItem("#121", "19.06.2010",
						"Show notes", "http://link"));
			}
		});
		
		sendKeys(KeyEvent.KEYCODE_DPAD_CENTER);

		assertPlayedPodcastAtLink("http://link");
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

	public void playPodcastUri(Uri uri) {
		playedPodcastUri = uri;
	}

	private void assertPlayedPodcastAtLink(String link) {
		assertEquals(Uri.parse(link), playedPodcastUri);
	}
}
