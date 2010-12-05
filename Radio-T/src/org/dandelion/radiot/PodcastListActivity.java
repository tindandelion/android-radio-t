package org.dandelion.radiot;

import java.util.List;

import org.dandelion.radiot.PodcastList.IPodcastListEngine;
import org.dandelion.radiot.PodcastList.IView;
import org.dandelion.radiot.home_screen.HomeScreenActivity;

import android.app.AlertDialog;
import android.app.ListActivity;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
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
	public static final String SHOW_NAME_KEY = "podcast_url";

	public static void start(Context context, String title, String showName) {
		Intent intent = new Intent(context, PodcastListActivity.class);
		intent.putExtra(SHOW_NAME_KEY, showName);
		intent.putExtra(TITLE_KEY, title);
		context.startActivity(intent);
	}

	private Bundle extras;

	private PodcastListAdapter listAdapter;
	private IPodcastPlayer podcastPlayer;
	private IPodcastListEngine engine;

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
		initList();
		setPodcastPlayer(new ExternalPlayer());
		attachToEngine();
	}

	private void initList() {
		ListView listView = (ListView) findViewById(android.R.id.list);
		int colorHint = getResources().getColor(R.color.theme_background);
		listView.setCacheColorHint(colorHint);
	}

	protected void attachToEngine() {
		RadiotApplication app = (RadiotApplication) getApplication();
		engine = (IPodcastListEngine) getLastNonConfigurationInstance();
		if (null == engine) {
			engine = app.getPodcastEngine(getFeedUrlFromExtra());
		}
		engine.attach(this);
	}

	@Override
	protected void onResume() {
		super.onResume();
		engine.refresh(false);
	}

	@Override
	protected void onStop() {
		super.onStop();
		engine.detach();
	}

	@Override
	protected void onDestroy() {
		if (null != engine) {
			engine.cancelUpdate();
		}
		super.onDestroy();
	}

	@Override
	public Object onRetainNonConfigurationInstance() {
		engine.detach();
		IPodcastListEngine savedPresenter = engine;
		engine = null;
		return savedPresenter;
	}

	@Override
	public boolean onCreateOptionsMenu(Menu menu) {
		MenuInflater inflater = getMenuInflater();
		inflater.inflate(R.menu.default_menu, menu);
		inflater.inflate(R.menu.podcast_list, menu);
		return super.onCreateOptionsMenu(menu);
	}

	@Override
	public boolean onOptionsItemSelected(MenuItem item) {
		switch (item.getItemId()) {
		case R.id.refresh:
			engine.refresh(true);
			return true;
		case R.id.go_home:
			HomeScreenActivity.start(this);
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
		return extras.getString(SHOW_NAME_KEY);
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
		podcastPlayer.startPlaying(this, uri);
	}

	class PodcastListAdapter extends ArrayAdapter<PodcastItem> {
		private final Bitmap defaultPodcastImage = BitmapFactory
				.decodeResource(PodcastListActivity.this.getResources(),
						R.drawable.default_podcast_image);

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
			ImageView image = (ImageView) row
					.findViewById(R.id.podcast_item_icon);
			Bitmap bitmap = item.getImage();
			if (null == bitmap) {
				bitmap = defaultPodcastImage;
			}
			image.setImageBitmap(bitmap);
		}

		private void setElementText(View row, int resourceId, String value) {
			TextView view = (TextView) row.findViewById(resourceId);
			view.setText(value);
		}
	}

	public IPodcastListEngine getPodcastListEngine() {
		return engine;
	}

	public void updatePodcastImage(int index) {
		listAdapter.notifyDataSetChanged();
	}
}
