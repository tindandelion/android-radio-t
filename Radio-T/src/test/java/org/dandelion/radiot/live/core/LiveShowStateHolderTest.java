package org.dandelion.radiot.live.core;

import org.junit.Test;

import static org.mockito.Mockito.*;

public class LiveShowStateHolderTest {
    private final long TIMESTAMP = 12345;
    private final LiveShowState state = LiveShowState.Idle;
    private final LiveShowStateHolder holder = new LiveShowStateHolder(state, TIMESTAMP);
    private final LiveShowStateListener listener = mock(LiveShowStateListener.class);

    @Test
    public void whenListenerAdded_instantlyCallItWithCurrentValue() throws Exception {
        holder.addListener(listener);
        verify(listener).onStateChanged(state, TIMESTAMP);
    }

    @Test
    public void whenListenerAddedSilently_doesNotCallItImmediately() throws Exception {
        holder.addListenerSilently(listener);
        verifyZeroInteractions(listener);
    }

    @Test
    public void callListenerWhenNewValueSet() throws Exception {
        final LiveShowState newValue = LiveShowState.Playing;
        final long newTimestamp = 45678;
        holder.addListener(listener);

        reset(listener);
        holder.setValue(newValue, newTimestamp);
        verify(listener).onStateChanged(newValue, newTimestamp);
    }

    @Test
    public void notifiesAllListeners() throws Exception {
        final LiveShowStateListener otherListener = mock(LiveShowStateListener.class);

        holder.addListener(listener);
        holder.addListener(otherListener);

        reset(listener, otherListener);
        holder.setValue(state, TIMESTAMP);

        verify(listener).onStateChanged(any(LiveShowState.class), anyLong());
        verify(otherListener).onStateChanged(any(LiveShowState.class), anyLong());
    }

    @Test
    public void removesListener() throws Exception {
        holder.addListener(listener);

        reset(listener);
        holder.removeListener(listener);
        holder.setValue(state, 0);

        verifyZeroInteractions(listener);
    }
}
