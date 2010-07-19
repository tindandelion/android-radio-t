package org.dandelion.radiot.test;

import java.lang.reflect.Field;

import org.dandelion.radiot.PodcastList.IModel;
import org.dandelion.radiot.PodcastListActivity;
import org.dandelion.radiot.test.helpers.ApplicationDriver;
import org.dandelion.radiot.test.helpers.NewBasicAcceptanceTestCase;

import android.view.View;
import android.widget.ListView;
import android.widget.TextView;

public class DisplayingPodcastList extends
		NewBasicAcceptanceTestCase {

	private PodcastListActivity activity;
	private ApplicationDriver appDriver;
	
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
		appDriver = createApplicationDriver();
		activity = appDriver.visitMainShowPage();
		testPresenter.assertTaskIsFinished();
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
		return activity.getListAdapter().getView(index, null, null);
	}

}
