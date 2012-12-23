package org.dandelion.radiot.live.ui;

import android.widget.ImageButton;
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
    private VisualResources strings;
    private ImageButton button;
    private TextView status;
    private TimerView timer;
    private TextView hint;
    private PlaybackControlView control;
    private PlaybackControlPresenter presenter;
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
        strings = new VisualResources(getResources());
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.live_playback_control, container, false);
        control = (PlaybackControlView) view.findViewById(R.id.playback_control);
        button = control.button();
        status = control.status();
        timer = control.timer();
        hint = (TextView) view.findViewById(R.id.live_hint_label);

        button.setOnClickListener(onTogglePlayback);

        return view;
    }

    @Override
    public void onStart() {
        super.onStart();
        client = LiveShowApp.getInstance().createClient(getActivity());
        presenter = new PlaybackControlPresenter(this);
        client.addListener(presenter);
    }

    @Override
    public void onStop() {
        timer.stop();
        client.removeListener(presenter);
        super.onStop();
    }

    public void setButtonState(int id, boolean enabled) {
        button.setImageResource(strings.buttonLabelForId(id));
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

    private static class VisualResources {
        private int[] buttonLabels = new int[] {
                R.drawable.ic_stop,
                R.drawable.ic_play
        };
        private String[] statusLabels;

        public VisualResources(android.content.res.Resources resources) {
            statusLabels = resources.getStringArray(R.array.live_show_status_labels);
        }

        public int buttonLabelForId(int id) {
            return buttonLabels[id];
        }

        public String statusLabelForId(int id) {
            return statusLabels[id];
        }
    }
}
