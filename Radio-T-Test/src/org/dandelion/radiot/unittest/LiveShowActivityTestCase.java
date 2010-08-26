package org.dandelion.radiot.unittest;

import org.dandelion.radiot.live.LiveShowService;
import org.dandelion.radiot.unittest.testables.TestableLiveShowActivity;

import android.content.Intent;
import android.test.ActivityUnitTestCase;

public class LiveShowActivityTestCase extends
		ActivityUnitTestCase<TestableLiveShowActivity> {

	private TestableLiveShowActivity activity;

	public LiveShowActivityTestCase() {
		super(TestableLiveShowActivity.class);
	}

	@Override
	protected void setUp() throws Exception {
		super.setUp();
		activity = startActivity(new Intent(), null, null);
		getInstrumentation().callActivityOnStart(activity);
	}

	public void testConnectsToServiceAtStart() throws Exception {
		assertTrue(activity.isServiceConnected());
	}
	
	public void testDisconnectsFromServiceAtStop() throws Exception {
		getInstrumentation().callActivityOnStop(activity);
		assertFalse(activity.isServiceConnected());
	}
	
	public void testStopsReceivingBroadcastsAtStop() throws Exception {
		getInstrumentation().callActivityOnStop(activity);
		getInstrumentation().getContext().sendBroadcast(
				new Intent(LiveShowService.PLAYBACK_STATE_CHANGED));
		assertFalse(activity.playbackStateChanged());
	}
	

	public void testReceivesServiceBroadcasts() throws Exception {
		getInstrumentation().getContext().sendBroadcast(
				new Intent(LiveShowService.PLAYBACK_STATE_CHANGED));
		assertTrue(activity.playbackStateChanged());
	}

}
