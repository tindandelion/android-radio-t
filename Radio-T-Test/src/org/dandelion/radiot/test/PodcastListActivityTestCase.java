package org.dandelion.radiot.test;

import java.util.ArrayList;
import java.util.Date;

import org.dandelion.radiot.PodcastItem;
import org.dandelion.radiot.PodcastListActivity;

import android.content.Intent;
import android.test.ActivityUnitTestCase;
import android.test.UiThreadTest;
import android.view.View;
import android.widget.TextView;

public class PodcastListActivityTestCase extends
		ActivityUnitTestCase<PodcastListActivity> {

	// This date is 19.06.2010 
	private static final Date SAMPLE_DATE = new Date(110, 05, 19);
	
	private PodcastListActivity activity;
	public PodcastListActivityTestCase() {
		super(PodcastListActivity.class);
	}

	@Override
	protected void setUp() throws Exception {
		super.setUp();
		new ArrayList<PodcastItem>();
		PodcastListActivity.useModel(null);
		activity = startActivity(new Intent(), null, null);
	}

	@Override
	protected void tearDown() throws Exception {
		PodcastListActivity.resetModel();
		super.tearDown();
	}

	@UiThreadTest
	public void testDisplayPodcastItem() throws Exception {
		ArrayList<PodcastItem> items = new ArrayList<PodcastItem>();
		items.add(new PodcastItem(121, SAMPLE_DATE,
				"Show notes", ""));
		
		activity.updatePodcasts(items);
		View listItem = activity.getListAdapter().getView(0, null, null);

		assertEquals("#121", getTextOfElement(listItem,
				org.dandelion.radiot.R.id.podcast_item_view_number));
		assertEquals("19.06.2010", getTextOfElement(listItem,
				org.dandelion.radiot.R.id.podcast_item_view_date));
		assertEquals("Show notes", getTextOfElement(listItem,
				org.dandelion.radiot.R.id.podcast_item_view_shownotes));
	}

	private String getTextOfElement(View view, int elementId) {
		TextView textView = (TextView) view.findViewById(elementId);
		return textView.getText().toString();
	}
}
