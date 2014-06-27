package org.dandelion.radiot.live.ui;

import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;
import org.dandelion.radiot.R;
import org.dandelion.radiot.http.Consumer;
import org.dandelion.radiot.http.DataEngine;

public class CurrentTopicFragment extends Fragment implements Consumer<String> {
    public static DataEngine.Factory trackerFactory = null;
    private TextView topicText;
    private DataEngine engine;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        engine = trackerFactory.create();
        engine.setDataConsumer(this);
    }

    @Override
    public void onResume() {
        super.onResume();
        engine.start();
    }

    @Override
    public void onDestroy() {
        engine.setDataConsumer(null);
        super.onDestroy();
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.current_topic, container, false);
        assert view != null;

        topicText = (TextView) view.findViewById(R.id.current_topic_text);
        return view;
    }

    @Override
    public void accept(final String newTopic) {
        getActivity().runOnUiThread(new Runnable() {
            @Override
            public void run() {
                topicText.setText(newTopic);
            }
        });

    }
}
