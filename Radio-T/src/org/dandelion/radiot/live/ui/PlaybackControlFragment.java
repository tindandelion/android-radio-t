package org.dandelion.radiot.live.ui;

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
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.live_playback_control, container, false);
        control = (PlaybackControlView) view.findViewById(R.id.playback_control);
        hint = (TextView) view.findViewById(R.id.live_hint_label);

        control.setButtonListener(onTogglePlayback);

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
        client.removeListener(presenter);
        super.onStop();
    }

    public void setButtonState(int id, boolean enabled) {
        control.setButtonState(id, enabled);
    }

    public void setStatusLabel(int id) {
        control.setStatusLabel(id);
    }

    public void startTimer(long timestamp) {
        control.startTimer(timestamp);
    }

    public void stopTimer() {
        control.stopTimer();
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

}
