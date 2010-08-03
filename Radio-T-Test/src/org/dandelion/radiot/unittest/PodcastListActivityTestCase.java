package org.dandelion.radiot.unittest;

import java.util.ArrayList;

import org.dandelion.radiot.PodcastItem;
import org.dandelion.radiot.PodcastList.IPodcastListEngine;
import org.dandelion.radiot.PodcastList.IView;
import org.dandelion.radiot.PodcastListActivity;
import org.dandelion.radiot.RadiotApplication;

import android.content.Intent;
import android.test.ActivityUnitTestCase;
import android.test.UiThreadTest;

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

		assertEquals(engine, activity.getPresenter());
		assertEquals(activity, engine.getView());
	}

	public void testAttachesToSavedPresenterOnCreation() throws Exception {
		NullPodcastEngine savedPresenter = new NullPodcastEngine();
		activity = startActivity(new Intent(), null, savedPresenter);
		assertEquals(savedPresenter, activity.getPresenter());
		assertEquals(activity, savedPresenter.getView());
	}

	public void testDetachesPresenterWhenReturningNonConfiurationInstance()
			throws Exception {
		activity = startActivity(new Intent(), null, null);
		
		NullPodcastEngine savedPresenter = (NullPodcastEngine) activity
				.onRetainNonConfigurationInstance();
		assertEquals(engine, savedPresenter);
		assertFalse(engine.isAttached());
		assertNull(activity.getPresenter());
	}

	public void testGetsShowNameFromBundleExtra() throws Exception {
		Intent intent = new Intent();
		intent.putExtra(PodcastListActivity.SHOW_NAME_KEY, "show-name");

		startActivity(intent, null, null);

		assertEquals("show-name", showName);

	}

	public void testGetsTitleFromExtra() throws Exception {
		Intent intent = new Intent();
		intent.putExtra(PodcastListActivity.TITLE_KEY, "Custom title");

		activity = startActivity(intent, null, null);

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
		setApplication(new RadiotApplication() {
			@Override
			public IPodcastListEngine getPodcastEngine(String feedUrl) {
				showName = feedUrl;
				return engine;
			}
		});
	}

	@Override
	protected void tearDown() throws Exception {
		super.tearDown();
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

		public boolean isAttached() {
			return null != view;
		}

		public void refresh(boolean resetCache) {
		}

		public void attach(IView view) {
			this.view = view;
		}
	}
}
