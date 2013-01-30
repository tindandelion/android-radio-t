package org.dandelion.radiot.live.ui;

import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import org.dandelion.radiot.R;
import android.os.Bundle;
import android.support.v4.app.ListFragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import org.dandelion.radiot.live.chat.ChatTranslation;
import org.dandelion.radiot.live.chat.ErrorListener;

public class ChatTranslationFragment extends ListFragment implements ErrorListener {
    public static ChatTranslation.Factory chatFactory;
    private static final int MESSAGE_LIMIT = 60;
    private static final int SHRINK_SIZE = 30;
    private ChatStreamAdapter adapter;
    private ChatTranslation chat;
    private View errorView;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.chat_translation, container, false);
        errorView = view.findViewById(R.id.chat_error_text);
        return view;
    }

    @Override
    public void onViewCreated(View view, Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        adapter = new ChatStreamAdapter(getActivity(), MESSAGE_LIMIT, SHRINK_SIZE);
        setListAdapter(adapter);
    }

    @Override
    public void onResume() {
        super.onResume();
        errorView.setVisibility(View.GONE);

        chat = chatFactory.create();
        ChatProgressListener progressListener = new ChatProgressListener(adapter, this);
        doResume(chat, adapter, (ChatStreamView) getListView(), progressListener);
    }

    public static void doResume(ChatTranslation translation, ChatStreamAdapter adapter, ChatStreamView listView, ErrorListener errorListener) {
        translation.start(new ChatScroller(adapter, listView), errorListener);
    }

    @Override
    public void onPause() {
        super.onPause();
        chat.stop();
    }

    @Override
    public void onError() {
        Animation animation = AnimationUtils.loadAnimation(getActivity(), R.anim.slide_in_down);
        errorView.setVisibility(View.VISIBLE);
        errorView.startAnimation(animation);
    }

    @Override
    public void onStarting() {

    }
}
