package org.dandelion.radiot.podcasts.ui;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.widget.ListView;
import org.dandelion.radiot.R;
import org.dandelion.radiot.RadiotApplication;
import org.dandelion.radiot.podcasts.PodcastsApp;
import org.dandelion.radiot.podcasts.core.PodcastListLoader;
import org.dandelion.radiot.util.CustomTitleListActivity;

public class PodcastListActivity extends CustomTitleListActivity {

    public static Intent createIntent(Context context, String title, String showName) {
        return StartParams.createIntent(context, title, showName);
    }

    private PodcastListLoader loader;

    @Override
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
        StartParams params = StartParams.fromIntent(getIntent());
        setTitle(params.title());
        initListView();

        PodcastListAdapter listAdapter = new PodcastListAdapter(this);
        setListAdapter(listAdapter);
        attachToEngine(params.showName(), listAdapter);
    }

    private void initListView() {
        ListView listView = getListView();
        setListBackground(listView);
        listView.setOnItemClickListener(createSelectionHandler());
    }

    private void setListBackground(ListView listView) {
        final int bgColor = getResources().getColor(R.color.window_background);

        listView.setCacheColorHint(bgColor);
        listView.setBackgroundColor(bgColor);
    }

    private PodcastSelectionHandler createSelectionHandler() {
        PodcastsApp app = PodcastsApp.getInstance();
        return new PodcastSelectionHandler(this, app.createPlayer(),
                app.createDownloader(), new DialogErrorDisplayer(this));
    }

    protected void attachToEngine(String showName, PodcastListAdapter listAdapter) {
		RadiotApplication app = (RadiotApplication) getApplication();
		loader = app.getPodcastEngine(showName);
		loader.attach(new ProgressDisplayer(this), listAdapter);
	}

	@Override
	protected void onResume() {
		super.onResume();
		loader.refresh(false);
	}
	
	@Override
	protected void onStop() {
		super.onStop();
		loader.detach();
	}

	@Override
	protected void onDestroy() {
		if (null != loader) {
			loader.cancelUpdate();
		}
		super.onDestroy();
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
			loader.refresh(true);
			return true;
		default:
			return super.onOptionsItemSelected(item);
		}
	}
}
