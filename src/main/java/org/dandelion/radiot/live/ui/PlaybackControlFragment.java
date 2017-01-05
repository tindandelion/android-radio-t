package org.dandelion.radiot.live.ui;

import android.os.Handler;
import android.widget.TextView;
import android.widget.ViewFlipper;
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
    private static final int HELP_TEXT_INDEX = 1;
    private static final int PLAYBACK_CONTROL_INDEX = 0;
    private static final long HELP_DISPLAY_DELAY = 3000;
    private Handler handler = new Handler();
    private PlaybackControlView control;
    private LiveShowClient client;

    private View.OnClickListener onTogglePlayback = new View.OnClickListener() {
        @Override
        public void onClick(View v) {
            client.togglePlayback();
        }
    };
    private ViewFlipper flipper;
    private TextView helpHint;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.live_playback_control, container, false);
        flipper = (ViewFlipper) view.findViewById(R.id.flipper);
        control = (PlaybackControlView) view.findViewById(R.id.playback_control);
        helpHint = (TextView) view.findViewById(R.id.live_hint_label);

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

    private void showHelpHint(int resourceId) {
        helpHint.setText(resourceId);
        flipper.setDisplayedChild(HELP_TEXT_INDEX);
        handler.postDelayed(new Runnable() {
            @Override
            public void run() {
                flipper.setDisplayedChild(PLAYBACK_CONTROL_INDEX);
            }
        }, HELP_DISPLAY_DELAY);
    }

    @Override
    public void onStateChanged(LiveShowState state, long timestamp) {
        if (state == LiveShowState.Waiting) {
            showHelpHint(R.string.live_show_waiting_hint);
        }
    }
}
