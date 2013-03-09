package org.dandelion.radiot.live.ui;

import org.dandelion.radiot.live.chat.ProgressListener;

class ChatProgressController implements ProgressListener {
    private final ChatTranslationFragment view;

    public ChatProgressController(ChatTranslationFragment view) {
        this.view = view;
    }

    @Override
    public void onConnecting() {
        view.hideError();
        view.showProgress();
    }

    @Override
    public void onConnected() {
        view.hideProgress();
    }

    @Override
    public void onError() {
        view.hideProgress();
        view.showError();
    }
}
