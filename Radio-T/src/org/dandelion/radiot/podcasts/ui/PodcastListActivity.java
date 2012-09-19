package org.dandelion.radiot.podcasts.ui;

import android.app.AlertDialog;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.widget.ListView;
import org.dandelion.radiot.R;
import org.dandelion.radiot.RadiotApplication;
import org.dandelion.radiot.podcasts.PodcastsApp;
import org.dandelion.radiot.podcasts.core.PodcastList.IPodcastListEngine;
import org.dandelion.radiot.podcasts.core.ProgressListener;
import org.dandelion.radiot.util.CustomTitleListActivity;

public class PodcastListActivity extends CustomTitleListActivity
        implements ProgressListener {

    public static Intent createIntent(Context context, String title, String showName) {
        return StartParams.createIntent(context, title, showName);
    }

    private IPodcastListEngine engine;
	private ProgressDialog progress;

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
        int bgColor = getResources().getColor(R.color.window_background);
        ListView listView = getListView();

        listView.setCacheColorHint(bgColor);
        listView.setBackgroundColor(bgColor);
        listView.setOnItemClickListener(createSelectionHandler());
    }

    private PodcastSelectionHandler createSelectionHandler() {
        PodcastsApp app = PodcastsApp.getInstance();
        return new PodcastSelectionHandler(this, app.createPlayer(),
                app.createDownloader(), this);
    }

    protected void attachToEngine(String showName, PodcastListAdapter listAdapter) {
		RadiotApplication app = (RadiotApplication) getApplication();
		engine = app.getPodcastEngine(showName);
		engine.attach(this, listAdapter);
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
		inflater.inflate(R.menu.podcast_list, menu);
		return super.onCreateOptionsMenu(menu);
	}

	@Override
	public boolean onOptionsItemSelected(MenuItem item) {
		switch (item.getItemId()) {
		case R.id.refresh:
			engine.refresh(true);
			return true;
		default:
			return super.onOptionsItemSelected(item);
		}
	}

    public void onError(String errorMessage) {
		new AlertDialog.Builder(this).setTitle(R.string.error_title)
				.setMessage(errorMessage).show();
	}

	public void onStarted() {
		progress = ProgressDialog.show(this, null,
				getString(R.string.loading_message), true, true,
				new DialogInterface.OnCancelListener() {
					public void onCancel(DialogInterface dialog) {
						finish();
					}
				});
	}

    public void onFinished() {
        progress.dismiss();
    }

    public IPodcastListEngine getPodcastListEngine() {
		return engine;
	}
}
