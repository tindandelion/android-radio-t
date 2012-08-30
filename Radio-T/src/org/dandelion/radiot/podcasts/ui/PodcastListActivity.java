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
import org.dandelion.radiot.home_screen.HomeScreenActivity;
import org.dandelion.radiot.podcasts.core.PodcastList.IPodcastListEngine;
import org.dandelion.radiot.util.CustomTitleActivity;

public class PodcastListActivity extends CustomTitleActivity {
	public static final String TITLE_EXTRA = "title";
	public static final String SHOW_NAME_EXTRA = "podcast_url";

    private PodcastListFragment fragment;

    public static void start(Context context, String title, String showName) {
		Intent intent = new Intent(context, PodcastListActivity.class);
		intent.putExtra(SHOW_NAME_EXTRA, showName);
		intent.putExtra(TITLE_EXTRA, title);
		context.startActivity(intent);
	}

	private Bundle extras;
    private IPodcastListEngine engine;


	@Override
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
        setContentView(R.layout.podcast_list_screen);
        initTitleFromExtras();
        initFragment();
		attachToEngine();
	}

    private void initFragment() {
        fragment = (PodcastListFragment) getSupportFragmentManager().findFragmentById(R.id.podcast_list);
    }

    private void initTitleFromExtras() {
        extras = getIntent().getExtras();
        setTitle(getTitleFromExtra());
    }

    protected void attachToEngine() {
		RadiotApplication app = (RadiotApplication) getApplication();
		engine = app.getPodcastEngine(getFeedUrlFromExtra());
		engine.attach(fragment);
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

    public ListView getListView() {
        return fragment.getListView();
    }

    public PodcastListAdapter getListAdapter() {
        return (PodcastListAdapter) fragment.getListAdapter();
    }
}
