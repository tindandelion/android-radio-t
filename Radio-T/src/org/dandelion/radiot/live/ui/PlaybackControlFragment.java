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
import org.dandelion.radiot.live.core.LiveShowState;
import org.dandelion.radiot.live.core.LiveShowStateListener;

public class PlaybackControlFragment extends Fragment implements LiveShowStateListener {
    private TextView hint;
    private PlaybackControlView control;
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
        client.addListener(this);
        client.addListener(control);
    }

    @Override
    public void onStop() {
        client.removeListener(control);
        client.removeListener(this);
        super.onStop();
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

    @Override
    public void onStateChanged(LiveShowState state, long timestamp) {
        showHelpText(state == LiveShowState.Waiting);
    }
}
