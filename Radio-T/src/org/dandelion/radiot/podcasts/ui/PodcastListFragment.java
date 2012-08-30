package org.dandelion.radiot.podcasts.ui;

import android.app.Activity;
import android.app.AlertDialog;
import android.app.ProgressDialog;
import android.content.DialogInterface;
import org.dandelion.radiot.R;
import android.os.Bundle;
import android.support.v4.app.ListFragment;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ListView;
import org.dandelion.radiot.podcasts.PodcastsApp;
import org.dandelion.radiot.podcasts.core.PodcastItem;
import org.dandelion.radiot.podcasts.core.PodcastList;

import java.util.List;

public class PodcastListFragment extends ListFragment implements PodcastList.IView {

    private PodcastListAdapter listAdapter;
    private ProgressDialog progress;
    private PodcastSelectionHandler selectionHandler;

    @Override
    public void onViewCreated(View view, Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        initListView();
        initListAdapter();
        initSelectionHandler();
    }

    private void initListAdapter() {
        listAdapter = new PodcastListAdapter(getActivity());
        setListAdapter(listAdapter);
    }

    private void initSelectionHandler() {
        selectionHandler = new PodcastSelectionHandler(PodcastsApp.getInstance().createPlayer(),
                PodcastsApp.getInstance().createDownloader(), this);
    }


    @Override
    public void updatePodcasts(List<PodcastItem> newList) {
        listAdapter.clear();
        for (PodcastItem item : newList) {
            listAdapter.add(item);
        }
    }

    @Override
    public void showProgress() {
        final Activity container = getActivity();
        progress = ProgressDialog.show(container, null,
                getString(R.string.loading_message), true, true,
                new DialogInterface.OnCancelListener() {
                    public void onCancel(DialogInterface dialog) {
                        container.finish();
                    }
                });
    }

    @Override
    public void closeProgress() {
        progress.dismiss();
    }

    @Override
    public void updatePodcastImage(int index) {
        listAdapter.notifyDataSetChanged();
    }

    public PodcastItem getItem(int position) {
        return listAdapter.getItem(position);
    }


    private void initListView() {
        int bgColor = getResources().getColor(R.color.window_background);
        ListView listView = getListView();
        listView.setCacheColorHint(bgColor);
        listView.setBackgroundColor(bgColor);
        listView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                PodcastItem selectedItem = getItem(position);
                selectionHandler.process(getActivity(), selectedItem);
            }
        });
    }

    @Override
    public void showErrorMessage(String errorMessage) {
        new AlertDialog.Builder(getActivity())
                .setTitle(R.string.error_title)
                .setMessage(errorMessage)
                .show();
    }
}
