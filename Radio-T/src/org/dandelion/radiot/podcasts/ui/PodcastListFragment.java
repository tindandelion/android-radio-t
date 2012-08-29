package org.dandelion.radiot.podcasts.ui;

import org.dandelion.radiot.R;
import android.os.Bundle;
import android.support.v4.app.ListFragment;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ListView;

public class PodcastListFragment extends ListFragment {

    @Override
    public void onViewCreated(View view, Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        initListView();
        initListAdapter();
    }

    private void initListAdapter() {
        setListAdapter(new PodcastListAdapter(getActivity()));
    }

    private void initListView() {
        int bgColor = getResources().getColor(R.color.window_background);
        ListView listView = getListView();
        listView.setCacheColorHint(bgColor);
        listView.setBackgroundColor(bgColor);
    }

    public void setOnItemClickListener(AdapterView.OnItemClickListener listener) {
        getListView().setOnItemClickListener(listener);
    }
}
