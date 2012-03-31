package org.dandelion.radiot.live.core;

import org.dandelion.radiot.live.core.states.LiveShowState;
import org.junit.Test;

import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.verify;

public class LiveShowStateHolderTest {
    private final LiveShowStateHolder holder = new LiveShowStateHolder();

    @Test
    public void callListenerWhenNewValueSet() throws Exception {
        LiveShowState newValue = new LiveShowState();
        LiveShowStateListener listener = mock(LiveShowStateListener.class);

        holder.setListener(listener);
        holder.setValue(newValue);
        verify(listener).onStateChanged(newValue);
    }
}
