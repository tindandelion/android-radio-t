package org.dandelion.radiot;

import android.app.Application;
import org.dandelion.radiot.live.ui.ChatTranslationFragment;
import org.dandelion.radiot.live.ui.HttpChatTranslation;
import org.dandelion.radiot.podcasts.main.PodcastsApp;

public class RadiotApplication extends Application {
    private static final String CHAT_URL = "http://chat.radio-t.com:18000";

    @Override
    public void onCreate() {
        super.onCreate();
        PodcastsApp.initialize(this);
        setupChatTranslation();
    }

    private void setupChatTranslation() {
        ChatTranslationFragment.chat = new HttpChatTranslation(CHAT_URL);
    }

    @Override
    public void onTerminate() {
        super.onTerminate();
        PodcastsApp.release();
    }
}
