package org.dandelion.radiot.home_screen;

import android.widget.TextView;
import org.dandelion.radiot.R;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import org.dandelion.radiot.util.AppInfo;
import org.dandelion.radiot.util.FeedbackEmail;

public class AppInfoFragment extends Fragment {
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        return customizeView(inflater.inflate(R.layout.app_info_view, container, false));
    }

    private View customizeView(View fragmentView) {
        setVersionLabel(fragmentView, appInfo().getVersion());
        attachFeedbackListener(fragmentView);
        return fragmentView;
    }

    private void attachFeedbackListener(View fragmentView) {
        View feedbackButton = fragmentView.findViewById(R.id.feedback_button);
        feedbackButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                new FeedbackEmail(getActivity(), appInfo()).openInEditor();
            }
        });
    }

    private void setVersionLabel(View fragmentView, String value) {
        TextView versionView = (TextView) fragmentView.findViewById(R.id.version_label);
        String template = getString(R.string.version_label);
        String version = String.format(template, value);
        versionView.setText(version);
    }

    private AppInfo appInfo() {
        return new AppInfo(getActivity());
    }
}
