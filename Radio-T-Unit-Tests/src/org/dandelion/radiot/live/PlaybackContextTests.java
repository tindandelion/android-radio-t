package org.dandelion.radiot.live;

import android.media.MediaPlayer;
import org.dandelion.radiot.live.core.PlaybackContext;
import org.dandelion.radiot.live.core.states.BasicState;
import org.junit.*;
import static org.junit.Assert.*;
import static org.mockito.Mockito.mock;

public class PlaybackContextTests {
    @Test
    public void creation() {
        MediaPlayer player = mock(MediaPlayer.class);
        BasicState.ILiveShowService service = mock(BasicState.ILiveShowService.class);
        PlaybackContext context = new PlaybackContext(service, player);
        assertNotNull(context);
    }
}
