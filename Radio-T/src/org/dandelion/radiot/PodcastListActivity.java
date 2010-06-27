package org.dandelion.radiot;

import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.List;

import org.dandelion.radiot.PodcastList.IPodcastListModel;

import android.app.ListActivity;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.ListView;
import android.widget.TextView;

public class PodcastListActivity extends ListActivity {
	private static final String PODCAST_URL = "http://feeds.rucast.net/radio-t";
	private static PodcastList.IPodcastListModel defaultModel;

	public static void resetModel() {
		defaultModel = null;
	}

	public static void useModel(IPodcastListModel podcastListModel) {
		defaultModel = podcastListModel;
	}

	private PodcastListAdapter listAdapter;
	private IPodcastListModel model;

	/** Called when the activity is first created. */
	@Override
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		listAdapter = new PodcastListAdapter();
		setListAdapter(listAdapter);
		model = getModel();
		refreshPodcasts();
	}

	private PodcastList.IPodcastListModel getModel() {
		if (null == defaultModel) {
			return new RssFeedModel(new RssFeedModel.UrlFeedSource(PODCAST_URL));
		}
		return defaultModel;
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
		try {
			updatePodcasts(model.retrievePodcasts());
		} catch (Exception e) {
			Log.e("RadioT", "Error updating podcast: "+ e.getMessage());
		}
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

