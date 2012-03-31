package org.dandelion.radiot.live.core;

import org.dandelion.radiot.live.core.states.LiveShowState;
import org.junit.Test;

import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.reset;
import static org.mockito.Mockito.verify;

public class LiveShowStateHolderTest {
    private final LiveShowState state = new LiveShowState();
    private final LiveShowStateHolder holder = new LiveShowStateHolder(state);
    private final LiveShowStateListener listener = mock(LiveShowStateListener.class);

    @Test
    public void callListenerWhenAttached() throws Exception {
        holder.setListener(listener);
        verify(listener).onStateChanged(state);
    }

    @Test
    public void callListenerWhenNewValueSet() throws Exception {
        LiveShowState newValue = new LiveShowState();
        holder.setListener(listener);

        reset(listener);
        holder.setValue(newValue);
        verify(listener).onStateChanged(newValue);
    }

}
