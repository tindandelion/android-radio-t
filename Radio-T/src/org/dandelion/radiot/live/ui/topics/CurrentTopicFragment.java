package org.dandelion.radiot.live.ui.topics;

import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;
import org.dandelion.radiot.R;

public class CurrentTopicFragment extends Fragment implements TopicListener {
    public static TopicTracker.Factory trackerFactory = null;
    private TextView topicText;
    private TopicTracker client;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        client = trackerFactory.create();
        client.setListener(this);
    }

    @Override
    public void onDestroy() {
        client.setListener(null);
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
    public void onTopicChanged(String newTopic) {
        topicText.setText(newTopic);
    }
}
