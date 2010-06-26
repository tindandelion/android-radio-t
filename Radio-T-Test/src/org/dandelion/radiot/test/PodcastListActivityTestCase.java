package org.dandelion.radiot.test;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import org.dandelion.radiot.PodcastItem;
import org.dandelion.radiot.PodcastListActivity;
import org.dandelion.radiot.PodcastListActivity.IPodcastProvider;

import android.content.Intent;
import android.test.ActivityUnitTestCase;
import android.test.UiThreadTest;
import android.view.View;
import android.widget.ListView;
import android.widget.TextView;

public class PodcastListActivityTestCase extends
		ActivityUnitTestCase<PodcastListActivity> implements IPodcastProvider {

	// This date is 19.06.2010 
	private static final Date SAMPLE_DATE = new Date(110, 05, 19);
	
	private PodcastListActivity activity;
	private ArrayList<PodcastItem> podcasts;

	public PodcastListActivityTestCase() {
		super(PodcastListActivity.class);
	}

	@Override
	protected void setUp() throws Exception {
		super.setUp();
		podcasts = new ArrayList<PodcastItem>();
		PodcastListActivity.usePodcastProvider(this);
		activity = startActivity(new Intent(), null, null);
	}
	
	@Override
	protected void tearDown() throws Exception {
		PodcastListActivity.resetPodcastProvider();
		super.tearDown();
	}

	@UiThreadTest
	public void testDisplayPodcastList() throws Exception {
		addPodcastItem(new PodcastItem(121));
		addPodcastItem(new PodcastItem(122));
		
		activity.refreshPodcasts();

		assertEquals(2, getListView().getCount());
	}

	@UiThreadTest
	public void testDisplayPodcastItem() throws Exception {
		PodcastItem item = new PodcastItem(121, SAMPLE_DATE,
				"Show notes", "");
		View listItem = setupOneItemList(item);

		assertEquals("#121", getTextOfElement(listItem,
				org.dandelion.radiot.R.id.podcast_item_view_number));
		assertEquals("19.06.2010", getTextOfElement(listItem,
				org.dandelion.radiot.R.id.podcast_item_view_date));
		assertEquals("Show notes", getTextOfElement(listItem,
				org.dandelion.radiot.R.id.podcast_item_view_shownotes));
	}

	private View setupOneItemList(PodcastItem item) {
		addPodcastItem(item);
		activity.refreshPodcasts();
		return getListView().getAdapter().getView(0, null, null);
	}

	private void addPodcastItem(PodcastItem item) {
		podcasts.add(item);
	}

	private String getTextOfElement(View view, int elementId) {
		TextView textView = (TextView) view.findViewById(elementId);
		return textView.getText().toString();
	}

	private ListView getListView() {
		return activity.getListView();
	}

	public List<PodcastItem> getPodcastList() {
		return podcasts;
	}
}
