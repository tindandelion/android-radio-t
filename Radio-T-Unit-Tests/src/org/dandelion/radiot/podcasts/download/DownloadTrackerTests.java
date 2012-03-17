package org.dandelion.radiot.podcasts.download;

import org.junit.Before;
import org.junit.Test;

import java.io.File;

import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertTrue;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.verifyZeroInteractions;

public class DownloadTrackerTests {

    private DownloadTracker tracker;
    private DownloadTask task;
    private DownloadTracker.PostProcessor postProcessor;

    @Before
    public void setUp() throws Exception {
        postProcessor = mock(DownloadTracker.PostProcessor.class);
        tracker = new DownloadTracker(postProcessor);
        task = new DownloadTask("", new File(""));
    }

    @Test
    public void testAddingTask() throws Exception {
        tracker.taskScheduled(1, task);
        assertTrue(tracker.hasScheduledTasks());
    }

    @Test
    public void testRemovingTask() throws Exception {
        tracker.taskScheduled(1, task);
        tracker.taskCompleted(1);
        assertFalse(tracker.hasScheduledTasks());
    }

    @Test
    public void ignoreUntrackedTasks() throws Exception {
        tracker.taskScheduled(1, task);
        tracker.taskCompleted(2);
        assertTrue(tracker.hasScheduledTasks());
    }

    @Test
    public void passesTaskToPostProcessor() throws Exception {
        tracker.taskScheduled(1, task);
        tracker.taskCompleted(1);
        verify(postProcessor).downloadComplete(task);
    }

    @Test
    public void testNotifiesListenerWhenAllTasksFinished() throws Exception {
        DownloadTracker.Listener listener = mock(DownloadTracker.Listener.class);
        tracker.setListener(listener);
        tracker.taskScheduled(1, task);
        tracker.taskScheduled(2, task);

        tracker.taskCompleted(1);
        verifyZeroInteractions(listener);

        tracker.taskCompleted(2);
        verify(listener).onAllTasksCompleted();
    }
}
