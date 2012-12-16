package org.dandelion.radiot.accepttest.testables;

import org.dandelion.radiot.helpers.async.NotificationTrace;
import org.dandelion.radiot.live.core.LiveShowState;
import org.dandelion.radiot.live.core.LiveShowStateListener;

import static org.hamcrest.CoreMatchers.equalTo;

public class FakeStatusDisplayer implements LiveShowStateListener {
    private NotificationTrace<LiveShowState> states = new NotificationTrace<LiveShowState>();

    public void showsStatusFor(LiveShowState expected) throws InterruptedException {
        states.receivedNotification(equalTo(expected));
    }

    @Override
    public void onStateChanged(LiveShowState state, long timestamp) {
        states.append(state);
    }
}
