package org.dandelion.radiot.endtoend.podcasts;

import android.support.test.rule.ActivityTestRule;
import android.support.test.runner.AndroidJUnit4;
import org.dandelion.radiot.podcasts.ui.PodcastListActivity;
import org.dandelion.radiot.podcasts.ui.PodcastListModel;
import org.junit.Rule;
import org.junit.Test;
import org.junit.runner.RunWith;

import static org.junit.Assert.fail;

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
    public void first() throws Exception {
        fail("This test runs fine");
    }

    private class TestPodcastPlatform implements PodcastListModel.Factory {

        @Override
        public PodcastListModel create(String showName) {
            return null;
        }
    }
}
