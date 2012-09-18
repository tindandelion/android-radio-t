package org.dandelion.radiot.unittest;

import android.content.Intent;
import android.test.ActivityUnitTestCase;
import android.test.UiThreadTest;
import org.dandelion.radiot.RadiotApplication;
import org.dandelion.radiot.podcasts.core.PodcastItem;
import org.dandelion.radiot.podcasts.core.PodcastList.IPodcastListEngine;
import org.dandelion.radiot.podcasts.core.PodcastList.IView;
import org.dandelion.radiot.podcasts.ui.PodcastListActivity;

import java.util.ArrayList;

public class PodcastListActivityTestCase extends
		ActivityUnitTestCase<PodcastListActivity> {

	protected String showName;
	private PodcastListActivity activity;
	private NullPodcastEngine engine;

	public PodcastListActivityTestCase() {
		super(PodcastListActivity.class);
	}

	public void testAttachesToNewPresenterOnCreation() {
		activity = startActivity(new Intent(), null, null);

		assertEquals(engine, activity.getPodcastListEngine());
		assertEquals(activity, engine.getView());
	}

	public void testConfiguresItselfFromStartParameters() throws Exception {
        Intent intent = PodcastListActivity.createIntent(getInstrumentation().getContext(),
                "Custom title", "show-name");

        activity = startActivity(intent, null, null);

		assertEquals("show-name", showName);
        assertEquals("Custom title", activity.getTitle());
    }

	@UiThreadTest
	public void testUpdatingPodcastList() throws Exception {
		activity = startActivity(new Intent(), null, null);
		assertEquals(0, activity.getListView().getCount());

		ArrayList<PodcastItem> newList = new ArrayList<PodcastItem>();
		PodcastItem itemToDisplay = new PodcastItem();
		newList.add(itemToDisplay);

		activity.updatePodcasts(newList);
		assertEquals(1, activity.getListView().getCount());
		Object displayedItem = activity.getListAdapter().getItem(0);

		assertEquals(itemToDisplay, displayedItem);
	}

	@Override
	protected void setUp() throws Exception {
		super.setUp();
		engine = new NullPodcastEngine();
        RadiotApplication application = new RadiotApplication() {
            @Override
            public IPodcastListEngine getPodcastEngine(String feedUrl) {
                showName = feedUrl;
                return engine;
            }
        };
        application.onCreate();
        setApplication(application);
	}

	class NullPodcastEngine implements IPodcastListEngine {
		private Object view;

		public void cancelUpdate() {
		}

		public void detach() {
			view = null;
		}

		public Object getView() {
			return view;
		}

		public void refresh(boolean resetCache) {
		}

		public void attach(IView view) {
			this.view = view;
		}
	}
}
