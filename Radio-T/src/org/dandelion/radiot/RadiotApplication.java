package org.dandelion.radiot;

import android.app.Application;
import android.os.Handler;
import org.dandelion.radiot.live.chat.ChatTranslation;
import org.dandelion.radiot.live.chat.PollingChatTranslation;
import org.dandelion.radiot.live.schedule.Scheduler;
import org.dandelion.radiot.live.ui.ChatTranslationFragment;
import org.dandelion.radiot.live.chat.HttpChatTranslation;
import org.dandelion.radiot.podcasts.main.PodcastsApp;

public class RadiotApplication extends Application {
    // private static final String CHAT_URL = "http://chat.radio-t.com:18000";
    private static final String CHAT_URL = "http://192.168.5.125:4567";

    @Override
    public void onCreate() {
        super.onCreate();
        PodcastsApp.initialize(this);
        setupChatTranslation();
    }

    private void setupChatTranslation() {
        ChatTranslationFragment.chat = createChatTranslation();
    }

    private ChatTranslation createChatTranslation() {
        HttpChatTranslation httpTranslation = new HttpChatTranslation(CHAT_URL);
        return new PollingChatTranslation(httpTranslation, new HandlerScheduler());
    }

    @Override
    public void onTerminate() {
        super.onTerminate();
        PodcastsApp.release();
    }

    private static class HandlerScheduler implements Scheduler {
        private Handler handler = new Handler();
        private Runnable action = new Runnable() {
            @Override
            public void run() {
                performer.performAction();
            }
        };
        private Performer performer;

        @Override
        public void setPerformer(Performer performer) {
            this.performer = performer;
        }

        @Override
        public void scheduleNext() {
            handler.postDelayed(action, 5000);
        }

        @Override
        public void cancel() {
            handler.removeCallbacks(action);
        }
    }
}
