package org.dandelion.radiot.live.ui;

import android.widget.ArrayAdapter;
import org.dandelion.radiot.live.chat.ErrorListener;

class ChatProgressListener implements ErrorListener {
    private final ArrayAdapter adapter;
    private final ErrorListener errorListener;

    public ChatProgressListener(ArrayAdapter adapter, ErrorListener errorListener) {
        this.adapter = adapter;
        this.errorListener = errorListener;
    }

    @Override
    public void onStarting() {
        adapter.clear();
    }

    @Override
    public void onError() {
        errorListener.onError();
    }
}
