package org.dandelion.radiot.live.ui;

import android.widget.Button;
import android.widget.TextView;
import org.dandelion.radiot.R;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

public class PlaybackControlFragment extends Fragment {
    private Button button;
    private String[] buttonLabels;
    private String[] statusLabels;
    private TextView status;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.playback_control_view, container, false);
        buttonLabels = getResources().getStringArray(
                R.array.live_show_button_labels);
        statusLabels = getResources().getStringArray(
                R.array.live_show_status_labels);
        button = (Button) view.findViewById(R.id.btn_toggle_live_playback);
        status = (TextView) view.findViewById(R.id.playback_state_label);
        return view;
    }

    public void setButtonState(int id, boolean enabled) {
        button.setText(buttonLabels[id]);
        button.setEnabled(enabled);
    }

    public void setOnClickListener(View.OnClickListener listener) {
        button.setOnClickListener(listener);
    }

    public void setStatusLabel(int id) {
        status.setText(statusLabels[id]);
    }
}
