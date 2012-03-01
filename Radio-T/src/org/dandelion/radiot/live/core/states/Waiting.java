package org.dandelion.radiot.live.core.states;

import org.dandelion.radiot.live.core.LiveShowQuery;
import org.dandelion.radiot.live.core.PlaybackContext;

public class Waiting extends PlaybackState {
    private static final int WAITING_NOTIFICATION_STRING_ID = 2;
    private Runnable onTimeout;

    public Waiting(PlaybackContext context) {
        super(context);
        onTimeout = new Runnable() {
            @Override
            public void run() {
                timeoutElapsed();
            }
        };
    }

    @Override
    public void enter() {
        context.playerReset();
        getService().setTimeout(waitTimeout, onTimeout);
        getService().goForeground(WAITING_NOTIFICATION_STRING_ID);
    }

    @Override
    public void leave() {
        getService().resetTimeout();
    }

    @Override
    public void acceptVisitor(LiveShowQuery visitor) {
        visitor.onWaiting(this);
    }

    public void timeoutElapsed() {
        context.connect();
    }
}
