package org.dandelion.radiot;

import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.List;

import org.dandelion.radiot.PodcastList.IPresenter;
import org.dandelion.radiot.PodcastList.IView;

import android.app.AlertDialog;
import android.app.ListActivity;
import android.app.ProgressDialog;
import android.content.Context;
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
import android.widget.ListView;
import android.widget.TextView;

public class PodcastListActivity extends ListActivity implements IView {

	public static final String URL_KEY = "podcast_url";
	public static final String TITLE_KEY = "title";
	private PodcastListAdapter listAdapter;
	private IPresenter presenter;

	private ProgressDialog progress;
	private Bundle extras;

	public void closeProgress() {
		progress.dismiss();
	}
	
	public static void start(Context context, String title, String url) {
		Intent intent = new Intent(context, PodcastListActivity.class);
		intent.putExtra(URL_KEY, url);
		intent.putExtra(TITLE_KEY, title);
		context.startActivity(intent);
	}

	/** Called when the activity is first created. */
	@Override
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		extras = getIntent().getExtras();
		setTitle(getTitleFromExtra());
		initListAdapter();
		presenter = PodcastList.getPresenter(this, getFeedUrlFromExtra());
		presenter.refreshData();
	}

	private String getTitleFromExtra() {
		if (null == extras) {
			return "";
		}
		return extras.getString(TITLE_KEY);
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
			refreshPodcasts();
			return true;
		default:
			return super.onOptionsItemSelected(item);
		}
	}

	public void refreshPodcasts() {
		presenter.refreshData();
	}

	public void showErrorMessage(String errorMessage) {
		new AlertDialog.Builder(this).setTitle("Error loading podcast feed")
				.setMessage(errorMessage).show();
	}

	public void showProgress() {
		progress = ProgressDialog.show(this, "", "Loading...");
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
			return "";
		}
		return extras.getString(URL_KEY);
	}

	private void initListAdapter() {
		listAdapter = new PodcastListAdapter();
		setListAdapter(listAdapter);
	}

	private void playPodcast(Uri uri) {
		Intent intent = new Intent(Intent.ACTION_VIEW);
		intent.setDataAndType(uri, "audio/mpeg");
		startActivity(intent);
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
			setElementText(row, R.id.podcast_item_view_number,
					formatNumber(item.getNumber()));
			setElementText(row, R.id.podcast_item_view_date,
					formatDateString(item.getPubDate()));
			setElementText(row, R.id.podcast_item_view_shownotes,
					item.getShowNotes());
			return row;
		}

		private String formatDateString(Date date) {
			SimpleDateFormat format = new SimpleDateFormat("dd.MM.yyyy");
			String strDate = format.format(date);
			return strDate;
		}

		private String formatNumber(int number) {
			return "#" + number;
		}

		private void setElementText(View row, int resourceId, String value) {
			TextView view = (TextView) row.findViewById(resourceId);
			view.setText(value);
		}
	}
}
