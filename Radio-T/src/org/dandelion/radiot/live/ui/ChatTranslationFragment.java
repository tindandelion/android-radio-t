package org.dandelion.radiot.live.ui;

import android.os.Bundle;
import android.support.v4.app.ListFragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import org.dandelion.radiot.R;
import org.dandelion.radiot.live.chat.ChatTranslation;
import org.dandelion.radiot.live.chat.Message;

import java.util.ArrayList;

public class ChatTranslationFragment extends ListFragment {
    public static ChatTranslation.Factory chatFactory;
    private static final int MESSAGE_LIMIT = 30;
    private ChatTranslation chat;
    private View errorView;
    private View progressView;
    private ArrayList<Message> messages;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setRetainInstance(true);
        chat = chatFactory.create();
        messages = new ArrayList<Message>();
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.chat_translation, container, false);
        errorView = view.findViewById(R.id.chat_error_text);
        progressView = view.findViewById(R.id.progress_container);
        return view;
    }

    @Override
    public void onViewCreated(View view, Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        ChatStreamAdapter adapter = new ChatStreamAdapter(getActivity(), messages, MESSAGE_LIMIT);
        setListAdapter(adapter);
        chat.setProgressListener(new ChatProgressController(adapter, this));
        chat.setMessageConsumer(new ChatScroller(adapter, getListView()));
    }

    @Override
    public ChatStreamView getListView() {
        return (ChatStreamView) super.getListView();
    }

    @Override
    public void onDestroyView() {
        super.onDestroyView();
        chat.setProgressListener(null);
        chat.setMessageConsumer(null);
    }

    @Override
    public void onResume() {
        super.onResume();
        chat.start();
    }

    @Override
    public void onPause() {
        super.onPause();
        chat.stop();
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
        chat.shutdown();
    }

    public void showError() {
        Animation animation = loadAnimation(R.anim.slide_in_down);
        errorView.setVisibility(View.VISIBLE);
        errorView.startAnimation(animation);
    }

    public void hideError() {
        errorView.setVisibility(View.GONE);
    }

    public void showProgress() {
        showViewAnimated(progressView);
        hideViewAnimated(getListView());
    }

    private Animation loadAnimation(int id) {
        return AnimationUtils.loadAnimation(getActivity(), id);
    }

    public void hideProgress() {
        hideViewAnimated(progressView);
        showViewAnimated(getListView());
    }

    private void hideViewAnimated(View view) {
        if (view.getVisibility() == View.VISIBLE) {
            view.startAnimation(loadAnimation(android.R.anim.fade_out));
            view.setVisibility(View.GONE);
        }
    }

    private void showViewAnimated(View view) {
        if (view.getVisibility() != View.VISIBLE) {
            view.startAnimation(loadAnimation(android.R.anim.fade_in));
            view.setVisibility(View.VISIBLE);
        }
    }
}
