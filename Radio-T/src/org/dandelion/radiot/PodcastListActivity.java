package org.dandelion.radiot;

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

	class PodcastListAdapter extends ArrayAdapter<PodcastItem> {

		public PodcastListAdapter(PodcastItem[] model) {
			super(PodcastListActivity.this, 0, model);
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

	private static final String LINK = "http://radio-t.com/downloads/rt_podcast190.mp3";

	private PodcastListAdapter listAdapter;

	/** Called when the activity is first created. */
	@Override
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setPodcastList(samplePodcastList());
	}

	private PodcastItem[] samplePodcastList() {
		return new PodcastItem[] {
				new PodcastItem("#121", "18.06.2010", "Show notes for 121",
						LINK),
				new PodcastItem("#122", "19.06.2010", "Show notes for 122",
						LINK),
				new PodcastItem("#123", "20.06.2010", "Show notes for 123",
						LINK) };
	}

	public void setPodcastList(PodcastItem[] podcastItems) {
		listAdapter = new PodcastListAdapter(podcastItems);
		setListAdapter(listAdapter);
	}

	@Override
	protected void onListItemClick(ListView l, View v, int position, long id) {
		super.onListItemClick(l, v, position, id);
		playPodcast(listAdapter.getItem(position)
				.getAudioUri());
	}

	private void playPodcast(Uri uri) {
		Intent intent = new Intent(Intent.ACTION_VIEW);
		intent.setDataAndType(uri, "audio/mpeg");
		startActivity(intent);
	}
}
