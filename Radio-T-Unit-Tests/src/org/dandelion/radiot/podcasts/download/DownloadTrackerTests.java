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
    private DownloadTask task;
    private DownloadProcessor nextProcessor;

    @Before
    public void setUp() throws Exception {
        nextProcessor = mock(DownloadProcessor.class);
        tracker = new DownloadTracker(nextProcessor);
        task = new DownloadTask().setId(1);
    }

    @Test
    public void testAddingTask() throws Exception {
        tracker.acceptTask(task);
        assertTrue(tracker.hasScheduledTasks());
    }

    @Test
    public void testRemovingTask() throws Exception {
        tracker.acceptTask(task);
        tracker.taskCompleted(1);
        assertFalse(tracker.hasScheduledTasks());
    }

    @Test
    public void ignoreUntrackedTasks() throws Exception {
        tracker.acceptTask(task);
        tracker.taskCompleted(2);
        assertTrue(tracker.hasScheduledTasks());
    }

    @Test
    public void passesTaskFurtherWhenItIsCompleted() throws Exception {
        tracker.acceptTask(task);
        tracker.taskCompleted(1);
        verify(nextProcessor).acceptTask(task);
    }

    @Test
    public void testNotifiesListenerWhenAllTasksFinished() throws Exception {
        DownloadTracker.Listener listener = mock(DownloadTracker.Listener.class);
        DownloadTask otherTask = new DownloadTask().setId(2);

        tracker.setListener(listener);
        tracker.acceptTask(task);
        tracker.acceptTask(otherTask);

        tracker.taskCompleted(1);
        verifyZeroInteractions(listener);

        tracker.taskCompleted(2);
        verify(listener).onAllTasksCompleted();
    }
}
