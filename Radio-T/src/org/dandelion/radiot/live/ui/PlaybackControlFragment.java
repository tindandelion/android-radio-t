package org.dandelion.radiot.live.ui;

import android.content.res.Resources;
import android.widget.Button;
import android.widget.TextView;
import org.dandelion.radiot.R;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import org.dandelion.radiot.live.LiveShowApp;
import org.dandelion.radiot.live.LiveShowClient;

public class PlaybackControlFragment extends Fragment {
    private StringResources strings;
    private Button button;
    private TextView status;
    private TimerView timer;
    private TextView hint;
    private LiveShowPresenter presenter;
    private LiveShowClient client;

    private View.OnClickListener onTogglePlayback = new View.OnClickListener() {
        @Override
        public void onClick(View v) {
            client.togglePlayback();
        }
    };

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        strings = new StringResources(getResources());
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.playback_control_view, container, false);
        button = (Button) view.findViewById(R.id.btn_toggle_live_playback);
        status = (TextView) view.findViewById(R.id.playback_state_label);
        timer = (TimerView) view.findViewById(R.id.live_timer_label);
        hint = (TextView) view.findViewById(R.id.live_hint_label);

        button.setOnClickListener(onTogglePlayback);

        return view;
    }

    @Override
    public void onStart() {
        super.onStart();
        client = LiveShowApp.getInstance().createClient(getActivity());
        presenter = new LiveShowPresenter(this);
        client.addListener(presenter);
    }

    @Override
    public void onStop() {
        timer.stop();
        client.removeListener(presenter);
        super.onStop();
    }

    public void setButtonState(int id, boolean enabled) {
        button.setText(strings.buttonLabelForId(id));
        button.setEnabled(enabled);
    }

    public void setStatusLabel(int id) {
        status.setText(strings.statusLabelForId(id));
    }

    public void startTimer(long timestamp) {
        timer.start(timestamp);
    }

    public void stopTimer() {
        timer.stop();
    }

    public void showHelpText(boolean visible) {
        int text = getHelpText(visible);
        hint.setText(text);
    }

    private int getHelpText(boolean visible) {
        if (visible) {
            return R.string.live_show_waiting_hint;
        } else {
            return R.string.live_show_info;
        }
    }

    private static class StringResources {
        private String[] buttonLabels;
        private String[] statusLabels;

        public StringResources(Resources resources) {
            buttonLabels = resources.getStringArray(R.array.live_show_button_labels);
            statusLabels = resources.getStringArray(R.array.live_show_status_labels);
        }

        public String buttonLabelForId(int id) {
            return buttonLabels[id];
        }

        public String statusLabelForId(int id) {
            return statusLabels[id];
        }
    }
}
