package org.dandelion.radiot.live.ui;

import android.app.Activity;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;
import org.dandelion.radiot.R;
import org.dandelion.radiot.http.Consumer;
import org.dandelion.radiot.http.DataEngine;
import org.dandelion.radiot.http.ProgressListener;

public class CurrentTopicFragment extends Fragment implements Consumer<String>,ProgressListener {
    public static DataEngine.Factory trackerFactory = null;
    private TextView topicText;
    private DataEngine engine;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        engine = trackerFactory.create();
        engine.setDataConsumer(this);
        engine.setProgressListener(this);
    }

    @Override
    public void onResume() {
        super.onResume();
        engine.start();
    }

    @Override
    public void onDestroy() {
        engine.setDataConsumer(null);
        engine.setProgressListener(null);
        super.onDestroy();
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.current_topic, container, false);
        topicText = (TextView) view.findViewById(R.id.current_topic_text);
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
                .show(this)
                .commit();
    }

    private void hideMyself() {
        getFragmentManager()
                .beginTransaction()
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
