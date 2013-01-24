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
    private ChatStreamAdapter adapter;
    private ChatTranslation myChat;
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
        myChat = chatFactory.create();

        adapter = new ChatStreamAdapter(getActivity());
        setListAdapter(adapter);
    }

    @Override
    public void onStart() {
        super.onStart();
        myChat.start(new ChatScroller(adapter, getListView()), this);
    }

    @Override
    public void onStop() {
        super.onStop();
        myChat.stop();
    }

    @Override
    public void onError() {
        Animation animation = AnimationUtils.loadAnimation(getActivity(), R.anim.slide_in_down);
        errorView.setVisibility(View.VISIBLE);
        errorView.startAnimation(animation);
    }
}
