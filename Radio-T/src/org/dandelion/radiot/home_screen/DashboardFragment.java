package org.dandelion.radiot.home_screen;

import android.content.Intent;
import android.widget.Button;
import org.dandelion.radiot.R;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import org.dandelion.radiot.live.ui.LiveShowActivity;
import org.dandelion.radiot.podcasts.ui.PodcastListActivity;

public class DashboardFragment extends Fragment {
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        return attachListeners(inflater.inflate(R.layout.dashboard_view, container, false));
    }

    private View attachListeners(View fragmentView) {
        onButtonClick(fragmentView, R.id.home_btn_main_show, new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                PodcastListActivity.start(getActivity(),
                        getString(R.string.main_show_title), "main-show");
            }
        });

        onButtonClick(fragmentView, R.id.home_btn_after_show, new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                PodcastListActivity.start(getActivity(),
                        getString(R.string.after_show_title), "after-show");
            }
        });

        onButtonClick(fragmentView, R.id.home_btn_live_show, new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                startActivity(new Intent(getActivity(), LiveShowActivity.class));
            }
        });
        onButtonClick(fragmentView, R.id.home_btn_about, new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                startActivity(new Intent(getActivity(), AboutAppActivity.class));
            }
        });
        return fragmentView;
    }

    private void onButtonClick(View fragmentView, int id, View.OnClickListener listener) {
        Button button = (Button) fragmentView.findViewById(id);
        button.setOnClickListener(listener);
    }
}
