package org.dandelion.radiot.live.ui;

import android.app.Activity;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageButton;
import android.widget.TextView;
import org.dandelion.radiot.R;
import org.dandelion.radiot.common.ui.Typefaces;
import org.dandelion.radiot.http.Consumer;
import org.dandelion.radiot.http.DataEngine;
import org.dandelion.radiot.http.ProgressListener;

public class CurrentTopicFragment extends Fragment implements Consumer<String>,ProgressListener {
    public static DataEngine.Factory trackerFactory = null;
    private TextView topicText;
    private DataEngine engine;
    private View.OnClickListener onHide = new View.OnClickListener() {
        @Override
        public void onClick(View v) {
            hideMyself();
        }
    };

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        engine = trackerFactory.create();
    }

    @Override
    public void onViewCreated(View view, Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        engine.setDataConsumer(this);
        engine.setProgressListener(this);
    }

    @Override
    public void onPause() {
        super.onPause();
        engine.stop();
    }

    @Override
    public void onResume() {
        super.onResume();
        engine.start();
    }

    @Override
    public void onDestroyView() {
        super.onDestroyView();
        engine.setDataConsumer(null);
        engine.setProgressListener(null);
    }

    @Override
    public void onDestroy() {
        engine.shutdown();
        super.onDestroy();
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.current_topic, container, false);
        topicText = (TextView) view.findViewById(R.id.current_topic_text);
        topicText.setTypeface(Typefaces.robotoLight(getActivity()));
        ImageButton hideButton = (ImageButton) view.findViewById(R.id.current_topic_hide);
        hideButton.setOnClickListener(onHide);
        return view;
    }

    @Override
    public void onAttach(Activity activity) {
        super.onAttach(activity);
        hideMyself();
    }

    @Override
    public void accept(final String newTopic) {
        topicText.setText(newTopic);
        showMyself();
    }

    private void showMyself() {
        getFragmentManager()
                .beginTransaction()
                .setCustomAnimations(R.anim.slide_in_up, R.anim.slide_out_down)
                .show(this)
                .commit();
    }

    private void hideMyself() {
        getFragmentManager()
                .beginTransaction()
                .setCustomAnimations(R.anim.slide_in_up, R.anim.slide_out_down)
                .hide(this)
                .commit();
    }

    @Override
    public void onConnecting() {

    }

    @Override
    public void onConnected() {

    }

    @Override
    public void onError() {
        hideMyself();
    }
}
