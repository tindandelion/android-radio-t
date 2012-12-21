package org.dandelion.radiot.live.ui;

import android.os.Bundle;
import android.support.v4.app.ListFragment;
import android.view.View;
import org.dandelion.radiot.live.chat.ChatTranslation;

public class ChatTranslationFragment extends ListFragment {
    public static ChatTranslation chat;
    private ChatStreamAdapter adapter;

    @Override
    public void onViewCreated(View view, Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        getListView().setDivider(null);

        adapter = new ChatStreamAdapter(getActivity());
        setListAdapter(adapter);
    }

    @Override
    public void onStart() {
        super.onStart();
        chat.requestLastRecords(adapter);
    }

    public void refreshChat() {
        chat.requestNextRecords(adapter);
    }
}
