package org.dandelion.radiot.unittest;

import org.dandelion.radiot.accepttest.TestLiveShowActivity;
import org.dandelion.radiot.live.LiveShowService;

import android.content.Intent;
import android.test.ActivityUnitTestCase;

public class LiveShowActivityTestCase extends
		ActivityUnitTestCase<TestLiveShowActivity> {

	private TestLiveShowActivity activity;

	public LiveShowActivityTestCase() {
		super(TestLiveShowActivity.class);
	}

	public void testConnectsToServiceAtStart() throws Exception {
		activity = startActivity(new Intent(), null, null);
		getInstrumentation().callActivityOnStart(activity);
		assertNotNull(activity.getService());
	}

	public void testReceivesServiceBroadcasts() throws Exception {
		activity = startActivity(new Intent(), null, null);
		getInstrumentation().callActivityOnStart(activity);
		getInstrumentation().getContext().sendBroadcast(
				new Intent(LiveShowService.PLAYBACK_STATE_CHANGED));
		assertTrue(activity.isPlaybackStateChanged());
	}

}
