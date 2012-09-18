package org.dandelion.radiot.podcasts.ui;

import android.app.AlertDialog;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.view.*;
import android.widget.ListView;
import org.dandelion.radiot.R;
import org.dandelion.radiot.RadiotApplication;
import org.dandelion.radiot.podcasts.PodcastsApp;
import org.dandelion.radiot.podcasts.core.PodcastItem;
import org.dandelion.radiot.podcasts.core.PodcastList.IPodcastListEngine;
import org.dandelion.radiot.podcasts.core.PodcastList.IView;
import org.dandelion.radiot.util.CustomTitleListActivity;

import java.util.List;

public class PodcastListActivity extends CustomTitleListActivity implements IView {
    public static Intent createIntent(Context context, String title, String showName) {
        return StartParams.createIntent(context, title, showName);
    }

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
        StartParams params = StartParams.fromIntent(getIntent());

        initListView();
        initListAdapter();
        initSelectionHandler();
        setTitle(params.title());
        attachToEngine(params.showName());
	}

    private void initListView() {
        int bgColor = getResources().getColor(R.color.window_background);
        getListView().setCacheColorHint(bgColor);
        getListView().setBackgroundColor(bgColor);
    }

    private void initSelectionHandler() {
        PodcastsApp app = PodcastsApp.getInstance();
        selectionHandler = new PodcastSelectionHandler(app.createPlayer(),
                app.createDownloader(), this);
    }

    protected void attachToEngine(String showName) {
		RadiotApplication app = (RadiotApplication) getApplication();
		engine = app.getPodcastEngine(showName);
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
        listAdapter.populateList(newList);
	}

	@Override
	protected void onListItemClick(ListView l, View v, int position, long id) {
        selectionHandler.process(this, listAdapter.getItem(position));
    }

    private void initListAdapter() {
		listAdapter = new PodcastListAdapter(this);
		setListAdapter(listAdapter);
	}

    public IPodcastListEngine getPodcastListEngine() {
		return engine;
	}

	public void updatePodcastImage(int index) {
		listAdapter.notifyDataSetChanged();
	}
}
