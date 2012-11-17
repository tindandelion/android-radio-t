package org.dandelion.radiot.live.ui;

import android.widget.Button;
import org.dandelion.radiot.R;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

public class PlaybackControlFragment extends Fragment {
    private Button button;
    private String[] buttonLabels;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.playback_control_view, container, false);
        buttonLabels = getResources().getStringArray(
                R.array.live_show_button_labels);
        button = (Button) view.findViewById(R.id.btn_toggle_live_playback);
        return view;
    }

    public void setButtonState(int id, boolean enabled) {
        button.setText(buttonLabels[id]);
        button.setEnabled(enabled);
    }

    public void setOnClickListener(View.OnClickListener listener) {
        button.setOnClickListener(listener);
    }
}
