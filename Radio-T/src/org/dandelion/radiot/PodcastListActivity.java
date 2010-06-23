package org.dandelion.radiot;

import java.util.ArrayList;

import android.app.ListActivity;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.ListView;
import android.widget.TextView;

public class PodcastListActivity extends ListActivity {
	public interface IPodcastProvider {
		public abstract void retrievePodcasts(PodcastListAdapter listAdapter);
	}	

	class PodcastListAdapter extends ArrayAdapter<PodcastItem> {

		public PodcastListAdapter(PodcastItem[] model) {
			super(PodcastListActivity.this, 0, model);
		}
		
		public PodcastListAdapter() {
			super(PodcastListActivity.this, 0, new ArrayList<PodcastItem>());
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
			setElementText(row, R.id.podcast_item_view_number, "#"
					+ item.getNumber());
			setElementText(row, R.id.podcast_item_view_date, item.getDate());
			setElementText(row, R.id.podcast_item_view_shownotes, item
					.getShowNotes());
			return row;
		}

		private void setElementText(View row, int resourceId, String value) {
			TextView view = (TextView) row.findViewById(resourceId);
			view.setText(value);
		}
	}

	private PodcastListAdapter listAdapter;
	private static IPodcastProvider podcastProvider;
	
	private static IPodcastProvider getPodcastProvider() {
		if (podcastProvider == null) {
			podcastProvider = new SamplePodcastProvider(); 
		} 
		return podcastProvider; 
	}
	
	public static void usePodcastProvider(IPodcastProvider provider) {
		podcastProvider = provider;
	}

	/** Called when the activity is first created. */
	@Override
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		listAdapter = new PodcastListAdapter();
		setListAdapter(listAdapter);
		refreshPodcasts();
	}
	
	public void refreshPodcasts() {
		listAdapter.clear();
		getPodcastProvider().retrievePodcasts(listAdapter);
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
}
