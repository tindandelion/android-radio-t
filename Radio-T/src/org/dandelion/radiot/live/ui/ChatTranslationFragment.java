package org.dandelion.radiot.live.ui;

import android.os.Bundle;
import android.support.v4.app.ListFragment;
import android.view.View;
import org.dandelion.radiot.live.chat.ChatTranslation;

public class ChatTranslationFragment extends ListFragment {
    public static ChatTranslation.Factory chatFactory;
    private ChatStreamAdapter adapter;
    private ChatTranslation myChat;

    @Override
    public void onViewCreated(View view, Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        myChat = chatFactory.create();

        getListView().setDivider(null);

        adapter = new ChatStreamAdapter(getActivity());
        setListAdapter(adapter);
    }

    @Override
    public void onStart() {
        super.onStart();
        myChat.start(adapter, errorListener());
    }

    private ChatTranslation.ErrorListener errorListener() {
        return new ChatTranslation.ErrorListener() {
            @Override
            public void onError() {
            }
        };
    }

    @Override
    public void onStop() {
        super.onStop();
        myChat.stop();
    }
}
