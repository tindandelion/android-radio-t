package org.dandelion.radiot;

import java.io.IOException;
import java.io.InputStream;
import java.net.URL;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.List;

import android.app.ListActivity;
import android.app.ProgressDialog;
import android.content.Intent;
import android.net.Uri;
import android.os.AsyncTask;
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

public class PodcastListActivity extends ListActivity {
	public interface IPodcastProvider {
		public abstract void refreshPodcasts(PodcastListActivity activity);
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

	private static final String PODCAST_URL = "http://feeds.rucast.net/radio-t";

	public static void resetPodcastProvider() {
		podcastProvider = null;
	}

	public static void usePodcastProvider(IPodcastProvider provider) {
		podcastProvider = provider;
	}

	private PodcastListAdapter listAdapter;

	private static IPodcastProvider podcastProvider;

	/** Called when the activity is first created. */
	@Override
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		listAdapter = new PodcastListAdapter();
		setListAdapter(listAdapter);
		refreshPodcasts();
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
		getPodcastProvider().refreshPodcasts(this);
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

	private IPodcastProvider getPodcastProvider() {
		if (podcastProvider == null) {
			podcastProvider = new RemoteRssProvider(PODCAST_URL);
		}
		return podcastProvider;
	}

	private void playPodcast(Uri uri) {
		Intent intent = new Intent(Intent.ACTION_VIEW);
		intent.setDataAndType(uri, "audio/mpeg");
		startActivity(intent);
	}
}

class RemoteRssProvider extends RssPodcastProvider {
	class RefreshTask extends AsyncTask<Void, Void, Void> {
		
		private PodcastListActivity activity;
		private List<PodcastItem> podcasts;
		private ProgressDialog progress;

		public RefreshTask(PodcastListActivity activity) {
			super();
			this.activity = activity;
		}
		@Override
		protected void onPreExecute() {
			progress = ProgressDialog.show(activity, "", "Loading");
		}

		@Override
		protected Void doInBackground(Void... params) {
			podcasts = retrievePodcasts();
			return null;
		}
		
		@Override
		protected void onPostExecute(Void result) {
			progress.dismiss();
			activity.updatePodcasts(podcasts);
		}

	}

	private String feedUrl;

	public RemoteRssProvider(String feedUrl) {
		this.feedUrl = feedUrl;
	}
	
	@Override
	protected InputStream openContentStream() throws IOException {
		URL url = new URL(feedUrl);
		return url.openStream();
	} 
	
	@Override
	public void refreshPodcasts(PodcastListActivity activity) {
		new RefreshTask(activity).execute();
	}
}

