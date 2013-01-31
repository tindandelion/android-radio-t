package org.dandelion.radiot.live.ui;

import android.widget.ArrayAdapter;
import org.dandelion.radiot.live.chat.ProgressListener;

class ChatProgressController implements ProgressListener {
    private final ArrayAdapter adapter;
    private final ChatTranslationFragment view;

    public ChatProgressController(ArrayAdapter adapter, ChatTranslationFragment view) {
        this.adapter = adapter;
        this.view = view;
    }

    @Override
    public void onConnecting() {
        view.hideError();
        adapter.clear();
    }

    @Override
    public void onError() {
        view.showError();
    }
}
