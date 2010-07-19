package org.dandelion.radiot.test;

import java.util.ArrayList;

import org.dandelion.radiot.PodcastItem;
import org.dandelion.radiot.PodcastList;
import org.dandelion.radiot.PodcastList.IModel;
import org.dandelion.radiot.PodcastList.IPresenter;
import org.dandelion.radiot.PodcastList.IView;
import org.dandelion.radiot.PodcastListActivity;

import android.content.Intent;
import android.test.ActivityUnitTestCase;
import android.test.UiThreadTest;

public class PodcastListActivityTestCase extends
		ActivityUnitTestCase<PodcastListActivity> {

	protected String feedSourceUrl;
	private PodcastListActivity activity;
	private NullPresenter presenter;

	public PodcastListActivityTestCase() {
		super(PodcastListActivity.class);
	}

	public void testAttachesToNewPresenterOnCreation() {
		activity = startActivity(new Intent(), null, null);

		assertEquals(presenter, activity.getPresenter());
		assertEquals(activity, presenter.getView());
	}

	public void testAttachesToSavedPresenterOnCreation() throws Exception {
		NullPresenter savedPresenter = new NullPresenter();
		activity = startActivity(new Intent(), null, savedPresenter);
		assertEquals(savedPresenter, activity.getPresenter());
		assertEquals(activity, savedPresenter.getView());
	}

	public void testDetachesPresenterWhenReturningNonConfiurationInstance()
			throws Exception {
		activity = startActivity(new Intent(), null, null);
		
		NullPresenter savedPresenter = (NullPresenter) activity
				.onRetainNonConfigurationInstance();
		assertEquals(presenter, savedPresenter);
		assertFalse(presenter.isAttached());
		assertNull(activity.getPresenter());
	}

	public void testGetsFeedUrlFromBundleExtra() throws Exception {
		Intent intent = new Intent();
		intent.putExtra(PodcastListActivity.URL_KEY, "podcast_feed_url");

		startActivity(intent, null, null);

		assertEquals("podcast_feed_url", feedSourceUrl);

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
		presenter = new NullPresenter();
		PodcastList.setFactory(new PodcastList.Factory() {
			@Override
			public IModel createModel(String url) {
				feedSourceUrl = url;
				return super.createModel(url);
			};

			@Override
			public IPresenter createPresenter(IModel model) {
				return presenter;
			}
		});
	}

	@Override
	protected void tearDown() throws Exception {
		PodcastList.resetFactory();
		super.tearDown();
	}

	class NullPresenter implements IPresenter {
		private Object view = new Object();

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

		public void refreshData(boolean resetCache) {
		}

		public void attach(IView view) {
			this.view = view;
		}
	}
}
