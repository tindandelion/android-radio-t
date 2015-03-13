package org.dandelion.radiot;

import android.app.Application;
import android.os.Handler;
import android.util.Log;
import org.dandelion.radiot.common.Scheduler;
import org.dandelion.radiot.http.DataEngine;
import org.dandelion.radiot.http.HttpDataEngine;
import org.dandelion.radiot.http.Provider;
import org.dandelion.radiot.live.chat.HttpChatClient;
import org.dandelion.radiot.live.topics.CurrentTopic;
import org.dandelion.radiot.live.ui.ChatTranslationFragment;
import org.dandelion.radiot.live.ui.CurrentTopicFragment;
import org.dandelion.radiot.podcasts.main.PodcastsApp;
import org.dandelion.radiot.util.ProgrammerError;

public class RadiotApplication extends Application {
    private static final String CHAT_URL = "http://chat.radio-t.com";
    private static final String TOPIC_TRACKER_BASE_URL = "http://107.170.84.215:8080";
    //private static final String TOPIC_TRACKER_BASE_URL = "http://10.0.1.2:8080";
    // private static final String CHAT_URL = "http://10.0.1.2:4567";

    public static DataEngine createChatTranslation(String chatUrl, Scheduler scheduler) {
        return new HttpDataEngine<>(HttpChatClient.create(chatUrl), scheduler);
    }

    @Override
    public void onCreate() {
        super.onCreate();
        PodcastsApp.initialize(this);
        setupChatTranslation();
        setupTopicTracker();
    }

    private void setupTopicTracker() {
        final int updateDelayMillis = 60000;
        CurrentTopicFragment.trackerFactory = new DataEngine.Factory() {
            @Override
            public DataEngine create() {
//                 Provider provider = new HttpTopicProvider(TOPIC_TRACKER_BASE_URL);
                Provider provider = new Provider<CurrentTopic>() {
                    @Override
                    public CurrentTopic get() throws Exception {
                        Log.d("TOPIC", "Requested new topic");
                        Thread.sleep(2000);
                        return new CurrentTopic("topic",
                                "Lorem ipsum dolor sit amet, consectetur adipiscing elit. Donec aliquet, massa ut tincidunt pellentesque, dui nibh lacinia magna, elementum ultrices magna risus a tellus");
                    }

                    @Override
                    public void abort() { }
                };
                return new HttpDataEngine(provider, new HandlerScheduler(updateDelayMillis));
            }
        };
    }

    private void setupChatTranslation() {
        final int updateDelayMillis = 5000;
        ChatTranslationFragment.chatFactory = new DataEngine.Factory() {
            @Override
            public DataEngine create() {
                return createChatTranslation(CHAT_URL, new HandlerScheduler(updateDelayMillis));
            }
        };
    }

    @Override
    public void onTerminate() {
        super.onTerminate();
        PodcastsApp.release();
    }

    private static class HandlerScheduler implements Scheduler {
        private final int delayMillis;
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

        private HandlerScheduler(int delayMillis) {
            this.delayMillis = delayMillis;
        }

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
            handler.postDelayed(action, delayMillis);
        }

        @Override
        public void cancel() {
            handler.removeCallbacks(action);
            isScheduled = false;
        }
    }
}
