package org.dandelion.radiot.live.ui;

import android.os.Bundle;
import android.support.v4.app.ListFragment;
import android.util.Log;
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
    public void onResume() {
        super.onResume();
        Log.d("CHAT", String.format("Start chat translation for: %d", hashCode()));
        myChat.start(adapter);
    }

    @Override
    public void onPause() {
        super.onPause();
        Log.d("CHAT", String.format("Stop chat translation for: %d", hashCode()));
        myChat.stop();
    }
}
