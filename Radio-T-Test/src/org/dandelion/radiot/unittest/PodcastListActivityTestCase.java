package org.dandelion.radiot.unittest;

import android.content.Intent;
import android.test.ActivityUnitTestCase;
import org.dandelion.radiot.RadiotApplication;
import org.dandelion.radiot.podcasts.core.PodcastList.IPodcastListEngine;
import org.dandelion.radiot.podcasts.core.PodcastListConsumer;
import org.dandelion.radiot.podcasts.core.ProgressListener;
import org.dandelion.radiot.podcasts.ui.PodcastListActivity;

public class PodcastListActivityTestCase extends
		ActivityUnitTestCase<PodcastListActivity> {

	protected String showName;
    private NullPodcastEngine engine;

	public PodcastListActivityTestCase() {
		super(PodcastListActivity.class);
	}

	public void testConfiguresItselfFromStartParameters() throws Exception {
        Intent intent = PodcastListActivity.createIntent(getInstrumentation().getContext(),
                "Custom title", "show-name");

        PodcastListActivity activity = startActivity(intent, null, null);

		assertEquals("show-name", showName);
        assertEquals("Custom title", activity.getTitle());
    }

	@Override
	protected void setUp() throws Exception {
		super.setUp();
		engine = new NullPodcastEngine();
        RadiotApplication application = new RadiotApplication() {
            @Override
            public IPodcastListEngine getPodcastEngine(String feedUrl) {
                showName = feedUrl;
                return engine;
            }
        };
        application.onCreate();
        setApplication(application);
	}

	class NullPodcastEngine implements IPodcastListEngine {
        public void cancelUpdate() {
		}

		public void detach() {
		}

        public void refresh(boolean resetCache) {
		}

		public void attach(ProgressListener view, PodcastListConsumer consumer) {
		}
    }
}
