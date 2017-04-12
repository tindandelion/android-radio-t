package org.dandelion.radiot.endtoend.podcasts;

import android.support.test.rule.ActivityTestRule;
import android.support.test.runner.AndroidJUnit4;
import org.dandelion.radiot.podcasts.core.PodcastItem;
import org.dandelion.radiot.podcasts.core.PodcastList;
import org.dandelion.radiot.podcasts.ui.PodcastListActivity;
import org.dandelion.radiot.podcasts.ui.PodcastListModel;
import org.dandelion.radiot.podcasts.ui.PodcastVisual;
import org.junit.Ignore;
import org.junit.Rule;
import org.junit.Test;
import org.junit.runner.RunWith;

import static android.support.test.espresso.Espresso.onData;
import static android.support.test.espresso.Espresso.onView;
import static android.support.test.espresso.action.ViewActions.click;
import static android.support.test.espresso.matcher.ViewMatchers.withText;
import static org.hamcrest.Matchers.instanceOf;

@RunWith(AndroidJUnit4.class)
public class PodcastDownloadingTest {

    @Rule
    public ActivityTestRule<PodcastListActivity> activityRule =
            new ActivityTestRule<PodcastListActivity>(PodcastListActivity.class) {
                @Override
                protected void beforeActivityLaunched() {
                    PodcastListActivity.modelFactory = new TestPodcastPlatform();
                }
            };

    @Test
    @Ignore
    public void downloadPodcast() throws Exception {
        onData(instanceOf(PodcastVisual.class))
                .atPosition(0)
                .perform(click());

        onView(withText("Загрузить"))
                .perform(click());

    }

    private class TestPodcastPlatform implements PodcastListModel.Factory, PodcastListModel {

        private Consumer consumer;

        @Override
        public PodcastListModel create(String showName) {
            return this;
        }

        @Override
        public void attach(ProgressListener progressListener, Consumer consumer) {
            this.consumer = consumer;
        }

        @Override
        public void release() {

        }

        @Override
        public void populateConsumer() {
            consumer.updateList(sampleList());
        }

        private PodcastList sampleList() {
            PodcastItem item = new PodcastItem();
            item.title = "111";
            item.showNotes = "Test notes";
            item.audioUri = "http://cdn.radio-t.com/rt_podcast540.mp3";

            PodcastList list = new PodcastList();
            list.add(item);
            return list;
        }

        @Override
        public void refreshData() {

        }
    }
}
