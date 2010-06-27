package org.dandelion.radiot.test;

import java.util.ArrayList;

import org.dandelion.radiot.PodcastListActivity;

import android.test.ActivityInstrumentationTestCase2;
import android.util.Log;
import android.widget.TextView;

import com.jayway.android.robotium.solo.Solo;

public class PodcastListDisplayTestCase extends
		ActivityInstrumentationTestCase2<PodcastListActivity> {

	private Solo solo;

	public PodcastListDisplayTestCase() {
		super("org.dandelion.radiot", PodcastListActivity.class);
	}
	
	@Override
	protected void setUp() throws Exception {
		super.setUp();
		solo = new Solo(getInstrumentation(), getActivity());
	}
	
	public void testPreConditions() throws Exception {
		solo.waitForDialogToClose(20000);
		ArrayList<TextView> views = solo.clickInList(4);
		for (TextView textView : views) {
			Log.i("RadioT", "Text is: " +  textView.getText());
		}
	}
}
