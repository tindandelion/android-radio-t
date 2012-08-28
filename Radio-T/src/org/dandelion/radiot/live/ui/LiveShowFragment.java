package org.dandelion.radiot.live.ui;

import android.widget.Button;
import android.widget.TextView;
import org.dandelion.radiot.R;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

public class LiveShowFragment extends Fragment {
    private TextView statusLabel;
    private String[] statusLabels;
    private String[] buttonLabels;
    private TimerView timerLabel;
    private View helpText;
    private Button controlButton;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        statusLabels = getResources().getStringArray(
                R.array.live_show_status_labels);
        buttonLabels = getResources().getStringArray(
                R.array.live_show_button_labels);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        return initFragment(inflater.inflate(R.layout.live_show_view, container, false));
    }

    private View initFragment(View view) {
        statusLabel = (TextView) view.findViewById(R.id.playback_state_label);
        timerLabel = (TimerView) view.findViewById(R.id.live_timer_label);
        helpText = view.findViewById(R.id.live_show_hint);
        controlButton = (Button) view.findViewById(R.id.live_show_action_button);
        return view;
    }

    public void setStatusLabel(int id) {
        statusLabel.setText(statusLabels[id]);
    }

    public void stopTimer() {
        timerLabel.stop();
    }

    public void startTimer(long timestamp) {
        timerLabel.start(timestamp);
    }

    public void showHelpText(boolean visible) {
        int visibility = (visible) ? View.VISIBLE : View.INVISIBLE;
        helpText.setVisibility(visibility);
    }

    public void setButtonState(int labelId, boolean enabled) {
        controlButton.setText(buttonLabels[labelId]);
        controlButton.setEnabled(enabled);
    }
}
