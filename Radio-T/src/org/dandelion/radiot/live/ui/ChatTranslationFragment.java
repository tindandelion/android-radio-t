package org.dandelion.radiot.live.ui;

import android.content.Context;
import android.os.Bundle;
import android.support.v4.app.ListFragment;
import android.view.View;
import android.widget.ArrayAdapter;
import org.dandelion.radiot.live.chat.ChatTranslation;

import java.util.List;

public class ChatTranslationFragment extends ListFragment {
    public static ChatTranslation chat;
    private ChatListAdapter adapter;

    @Override
    public void onViewCreated(View view, Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        adapter = new ChatListAdapter(getActivity());
        setListAdapter(adapter);
    }

    @Override
    public void onStart() {
        super.onStart();
        chat.requestLastRecords(adapter);
    }

    private static class ChatListAdapter extends ArrayAdapter<String>
            implements ChatTranslation.MessageConsumer {
        public ChatListAdapter(Context context) {
            super(context, android.R.layout.simple_list_item_1);
        }

        @Override
        public void addMessages(List<String> messages) {
            addAll(messages);
        }
    }
}
