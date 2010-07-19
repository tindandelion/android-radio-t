package org.dandelion.radiot;

import java.util.List;

import org.dandelion.radiot.PodcastList.IPresenter;
import org.dandelion.radiot.PodcastList.IView;

import android.app.AlertDialog;
import android.app.ListActivity;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.TextView;

public class PodcastListActivity extends ListActivity implements IView {

	public static final String TITLE_KEY = "title";
	public static final String URL_KEY = "podcast_url";

	public static void start(Context context, String title, String url) {
		Intent intent = new Intent(context, PodcastListActivity.class);
		intent.putExtra(URL_KEY, url);
		intent.putExtra(TITLE_KEY, title);
		context.startActivity(intent);
	}

	private Bundle extras;

	private PodcastListAdapter listAdapter;
	private IPodcastPlayer podcastPlayer;
	private IPresenter presenter;

	private ProgressDialog progress;

	public void closeProgress() {
		progress.dismiss();
	}

	/** Called when the activity is first created. */
	@Override
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		extras = getIntent().getExtras();
		setTitle(getTitleFromExtra());
		initListAdapter();
		setPodcastPlayer(new ExternalPlayer(this));
		attachToPresenter();
	}

	protected void attachToPresenter() {
		presenter = (IPresenter) getLastNonConfigurationInstance();
		if (null == presenter) {
			presenter = PodcastList.getPresenter(this, getFeedUrlFromExtra());
		}
		presenter.attach(this);
	}

	@Override
	protected void onResume() {
		super.onResume();
		presenter.refreshData(false);
	}

	@Override
	protected void onStop() {
		presenter.cancelUpdate();
		super.onStop();
	}

	@Override
	public Object onRetainNonConfigurationInstance() {
		presenter.detach();
		return presenter;
	}

	@Override
	public boolean onCreateOptionsMenu(Menu menu) {
		MenuInflater inflater = getMenuInflater();
		inflater.inflate(R.menu.podcast_list, menu);
		return super.onCreateOptionsMenu(menu);
	}

	@Override
	public boolean onOptionsItemSelected(MenuItem item) {
		switch (item.getItemId()) {
		case R.id.refresh:
			presenter.refreshData(true);
			return true;
		default:
			return super.onOptionsItemSelected(item);
		}
	}

	public void setPodcastPlayer(IPodcastPlayer player) {
		podcastPlayer = player;
	}

	public void showErrorMessage(String errorMessage) {
		new AlertDialog.Builder(this).setTitle(R.string.rss_load_error_title)
				.setMessage(errorMessage).show();
	}

	public void showProgress() {
		progress = ProgressDialog.show(this, null,
				getString(R.string.loading_message), true, true,
				new DialogInterface.OnCancelListener() {
					public void onCancel(DialogInterface dialog) {
						finish();
					}
				});
	}

	public void updatePodcasts(List<PodcastItem> newList) {
		listAdapter.clear();
		for (PodcastItem item : newList) {
			listAdapter.add(item);
		}
	}

	@Override
	protected void onListItemClick(ListView l, View v, int position, long id) {
		super.onListItemClick(l, v, position, id);
		playPodcast(listAdapter.getItem(position).getAudioUri());
	}

	private String getFeedUrlFromExtra() {
		if (null == extras) {
			return null;
		}
		return extras.getString(URL_KEY);
	}

	private String getTitleFromExtra() {
		if (null == extras) {
			return "";
		}
		return extras.getString(TITLE_KEY);
	}

	private void initListAdapter() {
		listAdapter = new PodcastListAdapter();
		setListAdapter(listAdapter);
	}

	private void playPodcast(Uri uri) {
		podcastPlayer.startPlaying(uri);
	}

	class PodcastListAdapter extends ArrayAdapter<PodcastItem> {

		public PodcastListAdapter() {
			super(PodcastListActivity.this, 0);
		}

		@Override
		public View getView(int position, View convertView, ViewGroup parent) {
			View row = convertView;
			if (row == null) {
				LayoutInflater inflater = getLayoutInflater();
				row = inflater.inflate(R.layout.podcast_list_item, parent,
						false);
			}

			return fillRowWithData(row, getItem(position));
		}

		private View fillRowWithData(View row, PodcastItem item) {
			setElementText(row, R.id.podcast_item_view_number, item.getNumber());
			setElementText(row, R.id.podcast_item_view_date, item.getPubDate());
			setElementText(row, R.id.podcast_item_view_shownotes,
					item.getShowNotes());
			setElementText(row, R.id.podcast_item_view_tags,
					item.getTagString());
			setPodcastIcon(row, item);
			return row;
		}

		private void setPodcastIcon(View row, PodcastItem item) {
			if (null != item.getImage()) {
				ImageView image = (ImageView) row
						.findViewById(R.id.podcast_item_icon);
				image.setImageDrawable(item.getImage());
			}
		}

		private void setElementText(View row, int resourceId, String value) {
			TextView view = (TextView) row.findViewById(resourceId);
			view.setText(value);
		}
	}

	public IPresenter getPresenter() {
		return presenter;
	}
}
