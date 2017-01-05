package org.dandelion.radiot.live.ui;

import android.widget.ArrayAdapter;
import org.dandelion.radiot.http.Consumer;
import org.dandelion.radiot.http.ProgressListener;

class ChatProgressController implements ProgressListener, Consumer<Exception> {
    private final ChatTranslationFragment view;
    private final ArrayAdapter adapter;

    public ChatProgressController(ChatTranslationFragment view, ArrayAdapter adapter) {
        this.adapter = adapter;
        this.view = view;
    }

    @Override
    public void onConnecting() {
        adapter.clear();
        view.hideError();
        view.showProgress();
    }

    @Override
    public void onConnected() {
        view.hideProgress();
    }

    @Override
    public void accept(Exception value) {
        view.hideProgress();
        view.showError();
    }
}
