package org.dandelion.radiot;

import android.app.Application;
import android.os.Handler;
import org.dandelion.radiot.common.Scheduler;
import org.dandelion.radiot.http.DataEngine;
import org.dandelion.radiot.http.HttpDataEngine;
import org.dandelion.radiot.http.Provider;
import org.dandelion.radiot.live.chat.ChatClient;
import org.dandelion.radiot.live.ui.ChatTranslationFragment;
import org.dandelion.radiot.live.ui.CurrentTopicFragment;
import org.dandelion.radiot.podcasts.main.PodcastsApp;
import org.dandelion.radiot.util.ProgrammerError;

public class RadiotApplication extends Application {
    private static final String CHAT_URL = "http://chat.radio-t.com";
    private static final String TOPIC_TRACKER_BASE_URL = "107.170.84.215:8080/chat";
    // private static final String CHAT_URL = "http://192.168.5.206:4567";

    public static DataEngine createChatTranslation(String chatUrl, Scheduler scheduler) {
        return new HttpDataEngine<>(ChatClient.create(chatUrl), scheduler);
    }

    private DataEngine createTopicTracker() {
        Provider<String> provider = new Provider<String>() {
            @Override
            public String get() throws Exception {
                return "";
            }

            @Override
            public void abort() {
            }
        };

        return new HttpDataEngine(provider, new HandlerScheduler());
    }

    @Override
    public void onCreate() {
        super.onCreate();
        PodcastsApp.initialize(this);
        setupChatTranslation();
        setupTopicTracker();
    }

    private void setupTopicTracker() {
        CurrentTopicFragment.trackerFactory = new DataEngine.Factory() {
            @Override
            public DataEngine create() {
                return createTopicTracker();
            }
        };
    }

    private void setupChatTranslation() {
        ChatTranslationFragment.chatFactory = new DataEngine.Factory() {
            @Override
            public DataEngine create() {
                return createChatTranslation(CHAT_URL, new HandlerScheduler());
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
