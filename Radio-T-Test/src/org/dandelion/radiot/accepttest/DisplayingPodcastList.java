package org.dandelion.radiot.accepttest;

import java.lang.reflect.Field;

import org.dandelion.radiot.R;

import org.dandelion.radiot.accepttest.drivers.HomeScreenDriver;
import org.dandelion.radiot.podcasts.ui.PodcastListActivity;
import org.dandelion.radiot.helpers.PodcastListAcceptanceTestCase;

import android.view.View;
import android.widget.ListView;
import android.widget.TextView;

public class DisplayingPodcastList extends
		PodcastListAcceptanceTestCase {

	private PodcastListActivity activity;
	private HomeScreenDriver appDriver;
	
	public void testDisplayPodcastItemInfo() throws Exception {
		View itemView = getItemView(0);

		assertTextFieldHasText(itemView, "podcast_item_view_number", "#5192");
		assertTextFieldHasText(itemView, "podcast_item_view_date", "11.02.2012");
	}

	public void testDisplayPodcastList() throws Exception {
		ListView list = activity.getListView();
		assertEquals(25, list.getCount());
	}

	@Override
	protected void setUp() throws Exception {
		super.setUp();
		appDriver = createDriver();
		activity = appDriver.visitMainShowPage();
		presenters.get(0).assertPodcastListIsUpdated();
	}

	private void assertTextFieldHasText(View parent, String id, String expected)
			throws Exception {
		TextView view = (TextView) parent.findViewById(getIdByName(id));
		assertEquals(expected, view.getText());
	}

	private int getIdByName(String id) throws Exception {
		Class<R.id> cls = R.id.class;
		Field field = cls.getDeclaredField(id);
        return field.getInt(cls);
	}

	private View getItemView(int index) {
		return activity.getListAdapter().getView(index, null, null);
	}

}
