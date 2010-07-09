package org.dandelion.radiot.test;

import java.lang.reflect.Field;

import org.dandelion.radiot.PodcastListActivity;

import android.test.ActivityInstrumentationTestCase2;
import android.view.View;
import android.widget.ListView;
import android.widget.TextView;

public class DisplayingPodcastList extends
		ActivityInstrumentationTestCase2<PodcastListActivity> {

	private PodcastListActivity activity;

	public DisplayingPodcastList() {
		super("org.dandelion.radiot", PodcastListActivity.class);
	}

	public void testDisplayPodcastItemInfo() throws Exception {
		View itemView = getItemView(0);

		assertTextFieldHasText(itemView, "podcast_item_view_number", "#5192");
		assertTextFieldHasText(itemView, "podcast_item_view_date", "20.06.2010");
		assertTextFieldHasText(itemView, "podcast_item_view_tags",
				"Подкасты, .me, ipad, Linux, LOR");

	}

	public void testDisplayPodcastList() throws Exception {
		ListView list = activity.getListView();
		assertEquals(17, list.getCount());
	}

	@Override
	protected void setUp() throws Exception {
		super.setUp();
		LocalRssFeedFactory.install(getInstrumentation());
		activity = getActivity();
	}

	private void assertTextFieldHasText(View parent, String id, String expected)
			throws Exception {
		TextView view = (TextView) parent.findViewById(getIdByName(id));
		assertEquals(expected, view.getText());
	}

	private int getIdByName(String id) throws Exception {
		Class<org.dandelion.radiot.R.id> cls = org.dandelion.radiot.R.id.class;
		Field field = cls.getDeclaredField(id);
		int i = field.getInt(cls);
		return i;
	}

	private View getItemView(int index) {
		return getActivity().getListAdapter().getView(index, null, null);
	}

}
