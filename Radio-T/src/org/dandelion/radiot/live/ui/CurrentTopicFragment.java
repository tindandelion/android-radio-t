package org.dandelion.radiot.live.ui;

import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentTransaction;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageButton;
import android.widget.TextView;
import org.dandelion.radiot.R;
import org.dandelion.radiot.common.ui.Typefaces;
import org.dandelion.radiot.http.Consumer;
import org.dandelion.radiot.http.DataEngine;
import org.dandelion.radiot.live.topics.CurrentTopic;

public class CurrentTopicFragment extends Fragment {
    public static DataEngine.Factory<CurrentTopic> trackerFactory = null;
    private TextView topicText;
    private DataEngine<CurrentTopic> engine;
    private Controller controller;

    private View.OnClickListener onHide = new View.OnClickListener() {
        @Override
        public void onClick(View v) {
            hideAnimated();
        }
    };

    private Consumer<Exception> onError = new Consumer<Exception>() {
        @Override
        public void accept(Exception value) {
            hideAnimated();
        }
    };

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        engine = trackerFactory.create();
        controller = new Controller(this);
    }

    @Override
    public void onSaveInstanceState(Bundle outState) {
        super.onSaveInstanceState(outState);
        controller.saveState(outState);
    }

    @Override
    public void onViewCreated(View view, Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        controller.onViewCreated(savedInstanceState);

        engine.setDataConsumer(controller);
        engine.setErrorConsumer(onError);
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
        engine.setErrorConsumer(null);
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

    public void setTopicText(CharSequence text) {
        topicText.setText(text);
    }

    public void showImmediately() {
        getFragmentTransaction(false).show(this).commit();
    }

    public void hideImmediately() {
        getFragmentTransaction(false).hide(this).commit();
    }

    public void showAnimated() {
        getFragmentTransaction(true).show(this).commit();
    }

    public void hideAnimated() {
        getFragmentTransaction(true).hide(this).commit();
    }

    private FragmentTransaction getFragmentTransaction(boolean animated) {
        FragmentTransaction t = getFragmentManager().beginTransaction();
        if (animated) t.setCustomAnimations(R.anim.slide_in_up, R.anim.slide_out_down);
        return t;
    }

    public CharSequence getTopicText() {
        return topicText.getText();
    }


    public static class Controller implements Consumer<CurrentTopic> {
        private static final String TOPIC_ID_KEY = "topic_id";
        public static final String TOPIC_TEXT_KEY = "topic_text";
        public static final String HIDDEN_KEY = "hidden";

        private final CurrentTopicFragment view;
        private String currentTopicId = "";

        public Controller(CurrentTopicFragment view) {
            this.view = view;
        }

        public void onViewCreated(Bundle savedState) {
            if (savedState == null) {
                view.hideImmediately();
            } else {
                restoreState(savedState);
            }

        }

        private void restoreState(Bundle savedState) {
            currentTopicId = savedState.getString(TOPIC_ID_KEY);

            if (savedState.getBoolean(HIDDEN_KEY, true)) {
                view.hideImmediately();
            } else {
                view.setTopicText(savedState.getCharSequence(TOPIC_TEXT_KEY));
                view.showImmediately();
            }
        }

        public void saveState(Bundle outState) {
            outState.putBoolean(HIDDEN_KEY, view.isHidden());
            outState.putString(TOPIC_ID_KEY, currentTopicId);
            outState.putCharSequence(TOPIC_TEXT_KEY, view.getTopicText());
        }

        @Override
        public void accept(CurrentTopic topic) {
            if (topic.isEmpty()) {
                reset();
            } else {
                updateTopic(topic);
            }
        }

        private void reset() {
            currentTopicId = "";
            view.hideAnimated();
        }

        private void updateTopic(CurrentTopic topic) {
            if (isNewTopic(topic)) displayTopicText(topic);
        }

        private boolean isNewTopic(CurrentTopic topic) {
            return !currentTopicId.equals(topic.id);
        }

        private void displayTopicText(CurrentTopic topic) {
            currentTopicId = topic.id;

            view.setTopicText(topic.text);
            view.showAnimated();
        }
    }
}

