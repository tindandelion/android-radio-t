package org.dandelion.radiot.podcasts.ui;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.widget.ListAdapter;
import android.widget.ListView;
import org.dandelion.radiot.R;
import org.dandelion.radiot.podcasts.loader.PodcastListClient;
import org.dandelion.radiot.podcasts.main.PodcastsApp;
import org.dandelion.radiot.util.CustomTitleActivity;

public class PodcastListActivity extends CustomTitleActivity {
    public static PodcastClientFactory clientFactory = null;

    private PodcastListClient client;
    private ProgressIndicator progress;

    public static Intent createIntent(Context context, String title, String showName) {
        return StartParams.createIntent(context, title, showName);
    }

    @Override
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
        setContentView(R.layout.podcast_screen);
        progress = ProgressIndicator.create(this);
    }

    @Override
    protected void onPostCreate(Bundle savedInstanceState) {
        super.onPostCreate(savedInstanceState);
        progress.onPostCreate(this);
        StartParams params = StartParams.fromIntent(getIntent());
        setTitle(params.title());
        PodcastListAdapter pa = new PodcastListAdapter(this);
        initListView(pa);
        attachToLoader(params.showName(), pa, progress);
    }

    private void initListView(PodcastListAdapter adapter) {
        ListView listView = getListView();
        listView.setAdapter(adapter);
        listView.setOnItemClickListener(createSelectionHandler());
    }

    private ListView getListView() {
        return (ListView) findViewById(R.id.podcast_list);
    }

    private PodcastSelectionHandler createSelectionHandler() {
        PodcastsApp app = PodcastsApp.getInstance();
        return new PodcastSelectionHandler(this, app.createPlayer(),
                app.createDownloader(), new DialogErrorDisplayer(this));
    }

    protected void attachToLoader(String show, PodcastListAdapter la, ProgressIndicator pi) {
		client = clientFactory.newClientForShow(show);
		client.attach(pi, la);
	}

	@Override
	protected void onResume() {
		super.onResume();
		client.refreshFromCache();
	}

    @Override
    protected void onDestroy() {
        super.onDestroy();
        client.release();
    }

    @Override
	public boolean onCreateOptionsMenu(Menu menu) {
		MenuInflater inflater = getMenuInflater();
		inflater.inflate(R.menu.podcast_list, menu);
        progress.onCreateOptionsMenu(menu);
		return super.onCreateOptionsMenu(menu);
	}

	@Override
	public boolean onOptionsItemSelected(MenuItem item) {
		switch (item.getItemId()) {
		case R.id.refresh:
            client.refreshFromServer();
			return true;
		default:
			return super.onOptionsItemSelected(item);
		}
	}

    public ListAdapter getListAdapter() {
        return getListView().getAdapter();
    }
}
