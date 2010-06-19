package org.dandelion.radiot.test;

import org.dandelion.radiot.PodcastListActivity;

import android.test.ActivityInstrumentationTestCase2;
import android.test.UiThreadTest;
import android.view.View;
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
		setPodcastListItems(new String[]{"#121", "#122"});
		
		assertEquals(2, getListView().getCount());
	}
	
	@UiThreadTest
	public void testDisplayPodcastItem() throws Exception {
		setPodcastListItems(new String[]{"#121"});
		
		TextView child = (TextView) getItemViewAt(0);
		assertEquals("#121", child.getText().toString());
	}

	private View getItemViewAt(int index) {
		return getListView().getAdapter().getView(index, null, null);
	}

	private ListView getListView() {
		return activity.getListView();
	}

	private void setPodcastListItems(String[] items) {
		activity.setPodcastList(items);
	}
	
}
