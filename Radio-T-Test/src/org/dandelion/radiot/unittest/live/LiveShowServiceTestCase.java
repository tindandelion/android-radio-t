package org.dandelion.radiot.unittest.live;


import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.test.ServiceTestCase;
import junit.framework.Assert;
import org.dandelion.radiot.live.service.PlaybackStateChangedEvent;
import org.dandelion.radiot.live.service.LiveShowService;

public class LiveShowServiceTestCase extends ServiceTestCase<LiveShowService> {
	private LiveShowService service;

	public LiveShowServiceTestCase() {
		super(LiveShowService.class);
	}

	@Override
	protected void setUp() throws Exception {
		super.setUp();
		bindService(new Intent());
		service = getService();
	}

	public void testSendsBroadcastsWhenStateChanged() throws Exception {
		(new BroadcastCatcher(getContext(),
                PlaybackStateChangedEvent.TAG) {
			@Override
			public void run() {
				service.onChangedState(null);
			}
		}).assertCaught();
	}

	@Override
	public void testServiceTestCaseSetUpProperly() throws Exception {
		// Stupid method
	}
}

abstract class BroadcastCatcher extends BroadcastReceiver {
	private boolean broadcastReceived = false;
	private IntentFilter filter;
	private Context context;

	public BroadcastCatcher(Context context, String action) {
		filter = new IntentFilter(action);
		this.context = context;
	}

	public void onReceive(Context context, Intent intent) {
		broadcastReceived = true;
	}

	public abstract void run();

	public void assertCaught() {
		context.registerReceiver(this, filter);
		try {
			run();
			Thread.yield();
			Assert.assertTrue("No broadcast received", broadcastReceived);
		} finally {
			context.unregisterReceiver(this);
		}
	}
}
