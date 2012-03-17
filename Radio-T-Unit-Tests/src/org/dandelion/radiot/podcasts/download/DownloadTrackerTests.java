package org.dandelion.radiot.podcasts.download;

import org.junit.Before;
import org.junit.Test;

import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertTrue;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.verifyZeroInteractions;

public class DownloadTrackerTests {

    private DownloadTracker tracker;

    @Before
    public void setUp() throws Exception {
        tracker = new DownloadTracker();
    }

    @Test
    public void testAddingTask() throws Exception {
        tracker.taskScheduled(1);
        assertTrue(tracker.hasScheduledTasks());
    }

    @Test
    public void testRemovingTask() throws Exception {
        tracker.taskScheduled(1);
        tracker.taskCompleted(1);
        assertFalse(tracker.hasScheduledTasks());
    }

    @Test
    public void ignoreUntrackedTasks() throws Exception {
        tracker.taskScheduled(1);
        tracker.taskCompleted(2);
        assertTrue(tracker.hasScheduledTasks());
    }

    @Test
    public void testNotifiesListenerWhenAllTasksFinished() throws Exception {
        DownloadTracker.Listener listener = mock(DownloadTracker.Listener.class);
        tracker.setListener(listener);
        tracker.taskScheduled(1);
        tracker.taskScheduled(2);

        tracker.taskCompleted(1);
        verifyZeroInteractions(listener);

        tracker.taskCompleted(2);
        verify(listener).onAllTasksCompleted();
    }
}
