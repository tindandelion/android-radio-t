package org.dandelion.radiot.podcasts.ui;

import android.app.AlertDialog;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.Bundle;
import android.view.*;
import android.widget.ArrayAdapter;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.TextView;
import org.dandelion.radiot.R;
import org.dandelion.radiot.RadiotApplication;
import org.dandelion.radiot.home_screen.HomeScreenActivity;
import org.dandelion.radiot.podcasts.PodcastsApp;
import org.dandelion.radiot.podcasts.core.PodcastItem;
import org.dandelion.radiot.podcasts.core.PodcastList.IPodcastListEngine;
import org.dandelion.radiot.podcasts.core.PodcastList.IView;
import org.dandelion.radiot.util.CustomTitleListActivity;

import java.util.List;

public class PodcastListActivity extends CustomTitleListActivity implements IView {

	public static final String TITLE_EXTRA = "title";
	public static final String SHOW_NAME_EXTRA = "podcast_url";

	public static void start(Context context, String title, String showName) {
		Intent intent = new Intent(context, PodcastListActivity.class);
		intent.putExtra(SHOW_NAME_EXTRA, showName);
		intent.putExtra(TITLE_EXTRA, title);
		context.startActivity(intent);
	}

	private Bundle extras;
	private PodcastListAdapter listAdapter;
    private IPodcastListEngine engine;
	private ProgressDialog progress;
    private PodcastSelectionHandler selectionHandler;

	public void closeProgress() {
		progress.dismiss();
	}

	@Override
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		extras = getIntent().getExtras();
		setTitle(getTitleFromExtra());
        initListView();
        initListAdapter();
        initSelectionHandler();
		attachToEngine();
	}

    private void initListView() {
        int bgColor = getResources().getColor(R.color.window_background);
        getListView().setCacheColorHint(bgColor);
        getListView().setBackgroundColor(bgColor);
    }

    private void initSelectionHandler() {
        selectionHandler = new PodcastSelectionHandler(PodcastsApp.getInstance().createPlayer(),
                PodcastsApp.getInstance().createDownloader(), this);
    }

    protected void attachToEngine() {
		RadiotApplication app = (RadiotApplication) getApplication();
		engine = app.getPodcastEngine(getFeedUrlFromExtra());
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

    public void showErrorMessage(String errorMessage) {
		new AlertDialog.Builder(this).setTitle(R.string.error_title)
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
        PodcastItem selectedItem = listAdapter.getItem(position);
        selectionHandler.process(this, selectedItem);
    }

    private String getFeedUrlFromExtra() {
		if (null == extras) {
			return null;
		}
		return extras.getString(SHOW_NAME_EXTRA);
	}

	private String getTitleFromExtra() {
		if (null == extras) {
			return "";
		}
		return extras.getString(TITLE_EXTRA);
	}

	private void initListAdapter() {
		listAdapter = new PodcastListAdapter();
		setListAdapter(listAdapter);
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
			setElementText(row, R.id.podcast_item_view_number, item.getNumberString());
			setElementText(row, R.id.podcast_item_view_date, item.getPubDate());
			setElementText(row, R.id.podcast_item_view_shownotes,
					item.getShowNotes());
			setPodcastIcon(row, item);
			return row;
		}

		private void setPodcastIcon(View row, PodcastItem item) {
			ImageView image = (ImageView) row
					.findViewById(R.id.podcast_item_icon);
			Bitmap bitmap = item.getThumbnail();
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
