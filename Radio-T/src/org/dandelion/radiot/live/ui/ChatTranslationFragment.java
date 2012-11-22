package org.dandelion.radiot.live.ui;

import org.dandelion.radiot.R;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

public class ChatTranslationFragment extends Fragment {
    public static ChatTranslation chat;
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        return inflater.inflate(R.layout.chat_translation, container, false);
    }

    @Override
    public void onStart() {
        super.onStart();
        chat.connect();
    }
}