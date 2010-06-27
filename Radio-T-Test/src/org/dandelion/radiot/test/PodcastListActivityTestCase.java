package org.dandelion.radiot.test;

import java.util.ArrayList;
import java.util.Date;

import org.dandelion.radiot.PodcastItem;
import org.dandelion.radiot.PodcastList.IPodcastListModel;
import org.dandelion.radiot.PodcastListActivity;
import org.dandelion.radiot.PodcastListActivity.IPodcastProvider;

import android.content.Intent;
import android.test.ActivityUnitTestCase;
import android.test.UiThreadTest;
import android.view.View;
import android.widget.ListView;
import android.widget.TextView;

public class PodcastListActivityTestCase extends
		ActivityUnitTestCase<PodcastListActivity> {

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
		PodcastListActivity.useModel(nullPodcastProvider());
		activity = startActivity(new Intent(), null, null);
	}

	private IPodcastProvider nullPodcastProvider() {
		return new IPodcastProvider() {
			public void refreshPodcasts(PodcastListActivity activity) {
			}
			public IPodcastListModel getModel() {
				return null;
			}
		};
	}
	
	@Override
	protected void tearDown() throws Exception {
		PodcastListActivity.resetResetModel();
		super.tearDown();
	}

	@UiThreadTest
	public void testDisplayPodcastList() throws Exception {
		podcasts.add(new PodcastItem(121));
		podcasts.add(new PodcastItem(122));
		
		activity.updatePodcasts(podcasts);

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
		podcasts.add(item);
		activity.updatePodcasts(podcasts);
		return getListView().getAdapter().getView(0, null, null);
	}

	private String getTextOfElement(View view, int elementId) {
		TextView textView = (TextView) view.findViewById(elementId);
		return textView.getText().toString();
	}

	private ListView getListView() {
		return activity.getListView();
	}
}
