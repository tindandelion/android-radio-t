package org.dandelion.radiot;

import android.app.Application;
import android.os.Handler;
import org.dandelion.radiot.live.chat.ChatTranslation;
import org.dandelion.radiot.live.chat.http.HttpTranslationEngine;
import org.dandelion.radiot.live.schedule.Scheduler;
import org.dandelion.radiot.live.ui.ChatTranslationFragment;
import org.dandelion.radiot.podcasts.main.PodcastsApp;
import org.dandelion.radiot.util.ProgrammerError;

public class RadiotApplication extends Application {
    private static final String CHAT_URL = "http://chat.radio-t.com:18000";
    // private static final String CHAT_URL = "http://192.168.5.206:4567";

    @Override
    public void onCreate() {
        super.onCreate();
        PodcastsApp.initialize(this);
        setupChatTranslation();
    }

    private void setupChatTranslation() {
        ChatTranslationFragment.chatFactory = new ChatTranslation.Factory() {
            @Override
            public ChatTranslation create() {
                return new HttpTranslationEngine(CHAT_URL, new HandlerScheduler());
            }
        };
    }

    @Override
    public void onTerminate() {
        super.onTerminate();
        PodcastsApp.release();
    }

    private static class HandlerScheduler implements Scheduler {
        private Handler handler = new Handler();
        private Performer performer;
        private boolean isScheduled = false;
        private Runnable action = new Runnable() {
            @Override
            public void run() {
                performer.performAction();
                isScheduled = false;
            }
        };

        @Override
        public void setPerformer(Performer performer) {
            this.performer = performer;
        }

        @Override
        public void scheduleNext() {
            if (isScheduled) {
                throw new ProgrammerError("The previous action hasn't finished yet");
            }

            isScheduled = true;
            handler.postDelayed(action, 5000);
        }

        @Override
        public void cancel() {
            handler.removeCallbacks(action);
            isScheduled = false;
        }
    }
}
