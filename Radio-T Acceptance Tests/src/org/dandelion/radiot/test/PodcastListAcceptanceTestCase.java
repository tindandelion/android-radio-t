package org.dandelion.radiot.test;

import java.util.ArrayList;

import org.dandelion.radiot.PodcastListActivity;

import android.test.ActivityInstrumentationTestCase2;
import android.view.View;
import android.widget.TextView;

import com.jayway.android.robotium.solo.Solo;

public class PodcastListAcceptanceTestCase extends
		ActivityInstrumentationTestCase2<PodcastListActivity> {

	private Solo solo;

	public PodcastListAcceptanceTestCase() {
		super("org.dandelion.radiot", PodcastListActivity.class);
	}

	@Override
	protected void setUp() throws Exception {
		super.setUp();
		LocalRssFeedFactory.install(getInstrumentation());
		solo = new Solo(getInstrumentation(), getActivity());
	}

	public void testDisplayPodcastItem() throws Exception {
		ArrayList<TextView> textViews = getTextViewsForItem(0);

		CharSequence number = textViews.get(0).getText();
		CharSequence date = textViews.get(1).getText();

		assertEquals("#5192", number);
		assertEquals("20.06.2010", date);
	}

	public void testDisplayTags() throws Exception {
		ArrayList<TextView> textViews = getTextViewsForItem(0);
		CharSequence tags = textViews.get(3).getText();

		assertEquals("Подкасты, .me, ipad, Linux, LOR", tags);
	}

	private ArrayList<TextView> getTextViewsForItem(int index) {
		View itemView = getActivity().getListAdapter().getView(index, null,
				null);
		ArrayList<TextView> textViews = solo.getCurrentTextViews(itemView);
		return textViews;
	}

}
