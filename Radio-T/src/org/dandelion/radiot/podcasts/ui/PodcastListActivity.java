package org.dandelion.radiot.podcasts.ui;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.support.v4.widget.SwipeRefreshLayout;
import android.widget.ListAdapter;
import android.widget.ListView;
import org.dandelion.radiot.R;
import org.dandelion.radiot.common.ui.CustomTitleActivity;
import org.dandelion.radiot.podcasts.core.PodcastAction;
import org.dandelion.radiot.podcasts.core.PodcastItem;
import org.dandelion.radiot.podcasts.loader.PodcastListClient;
import org.dandelion.radiot.podcasts.loader.ProgressListener;
import org.dandelion.radiot.podcasts.main.PodcastsApp;

public class PodcastListActivity extends CustomTitleActivity {
    public static PodcastClientFactory clientFactory = null;

    public final SwipeRefreshLayout.OnRefreshListener onRefresh = new SwipeRefreshLayout.OnRefreshListener() {
        @Override
        public void onRefresh() {
            client.refreshData();
        }
    };
    private final ProgressListener progressListener = new ProgressListener() {
        @Override
        public void onStarted() {
            refreshLayout.setRefreshing(true);
        }

        @Override
        public void onFinished() {
            refreshLayout.setRefreshing(false);
        }

        @Override
        public void onError(String errorMessage) {
            refreshLayout.setRefreshing(false);
            showErrorMessage(errorMessage);
        }

        private void showErrorMessage(String errorMessage) {
            DialogErrorDisplayer.showError(PodcastListActivity.this, errorMessage);
        }
    };


    private SwipeRefreshLayout refreshLayout;
    private PodcastListClient client;

    public static Intent createIntent(Context context, String title, String showName) {
        return StartParams.createIntent(context, title, showName);
    }

    @Override
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
        setContentView(R.layout.podcast_screen);
    }

    @Override
    protected void onPostCreate(Bundle savedInstanceState) {
        super.onPostCreate(savedInstanceState);
        StartParams params = StartParams.fromIntent(getIntent());
        setTitle(params.title());
        PodcastListAdapter pa = new PodcastListAdapter(this);
        initListView(pa);
        initRefreshLayout();
        attachToLoader(params.showName(), pa);
    }

    private void initRefreshLayout() {
        refreshLayout = (SwipeRefreshLayout) findViewById(R.id.refresh);
        refreshLayout.setOnRefreshListener(onRefresh);
        refreshLayout.setSize(SwipeRefreshLayout.LARGE);
        refreshLayout.setProgressBackgroundColorSchemeResource(R.color.background_light_blueish);
        refreshLayout.setColorSchemeResources(R.color.blue);
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
        return new PodcastSelectionHandler(this,
                trackingAction("play", app.createPlayer()),
                trackingAction("download", app.createDownloader()));
    }

    private PodcastAction trackingAction(final String label, final PodcastAction action) {
        return new PodcastAction() {
            @Override
            public void perform(Context context, PodcastItem podcast) {
                trackEvent("podcast_list", label);
                action.perform(context, podcast);
            }
        };
    }

    protected void attachToLoader(String show, PodcastListAdapter la) {
		client = clientFactory.newClientForShow(show);
        client.attach(progressListener, la);
	}

    @Override
	protected void onResume() {
		super.onResume();
        refreshLayout.post(new Runnable() {
            @Override
            public void run() {
                client.populateData();
            }
        });
	}

    @Override
    protected void onDestroy() {
        super.onDestroy();
        client.release();
    }

    public ListAdapter getListAdapter() {
        return getListView().getAdapter();
    }
}
