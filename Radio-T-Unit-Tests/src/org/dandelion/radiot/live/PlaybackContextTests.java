package org.dandelion.radiot.live;

import org.dandelion.radiot.live.core.PlaybackContext;
import org.junit.*;
import static org.junit.Assert.*;

public class PlaybackContextTests {
    final PlaybackContext context = new PlaybackContext(null, null);

    @Test
    public void creation() {
        assertNotNull(context);
    }
}
