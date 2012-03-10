package org.dandelion.radiot.unittest.live;

import android.content.Intent;
import android.test.ActivityUnitTestCase;
import org.dandelion.radiot.live.core.PlaybackStateChangedEvent;
import org.dandelion.radiot.live.core.states.LiveShowState;
import org.dandelion.radiot.unittest.testables.TestableLiveShowActivity;

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
		Thread.sleep(500); // Give the service some time to start up
	}

	public void testConnectsToServiceAtStart() throws Exception {
		assertTrue(activity.isServiceConnected());
	}

	public void testUpdatesViewAtStart() throws Exception {
		assertTrue(activity.isVisualStateUpdated);
	}

	public void testDisconnectsFromServiceAtStop() throws Exception {
		callOnStop();
		assertFalse(activity.isServiceConnected());
	}

	private void callOnStop() {
		getInstrumentation().callActivityOnStop(activity);
	}

	public void testStopsReceivingBroadcastsAtStop() throws Exception {
		activity.resetVisualState();
		callOnStop();
        PlaybackStateChangedEvent.send(getInstrumentation().getContext(), new LiveShowState());
		assertFalse(activity.isVisualStateUpdated);
	}

	public void testReceivesServiceBroadcasts() throws Exception {
		activity.resetVisualState();
        PlaybackStateChangedEvent.send(getInstrumentation().getContext(), new LiveShowState());
		assertTrue(activity.isVisualStateUpdated);
	}

}
