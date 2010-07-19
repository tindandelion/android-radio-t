package org.dandelion.radiot.test.helpers;

import java.util.concurrent.CountDownLatch;
import java.util.concurrent.TimeUnit;

import junit.framework.Assert;

import org.dandelion.radiot.PodcastList.IModel;
import org.dandelion.radiot.PodcastListPresenter;

public class TestPresenter extends PodcastListPresenter.AsyncPresenter {
	private static final int WAIT_TIMEOUT = 60;
	private CountDownLatch taskCancelLatch;
	private CountDownLatch taskFinishLatch;
	private int startedTasksCount;
	private int finishedTasksCount;

	public TestPresenter(IModel model) {
		super(model);
		taskCancelLatch = new CountDownLatch(1);
		taskFinishLatch = new CountDownLatch(1);
		startedTasksCount = 0;
		finishedTasksCount = 0;
	}

	@Override
	public void taskStarted() {
		super.taskStarted();
		startedTasksCount += 1;
	}

	@Override
	public void taskFinished(UpdateProgress progress) {
		super.taskFinished(progress);
		finishedTasksCount += 1;
		taskFinishLatch.countDown();
	}

	@Override
	public void taskCancelled() {
		super.taskCancelled();
		if (null != taskCancelLatch) {
			taskCancelLatch.countDown();
		}
		finishedTasksCount += 1;
	}
	
	public void assertStartedBackgroundTasksCount(int i) {
		Assert.assertEquals(i, startedTasksCount);
	}

	public void assertNoTasksAreActive() {
		if (startedTasksCount > finishedTasksCount) {
			Assert.fail("Not all background tasks were finished by tear down");
		}
	}

	public void assertTaskIsCancelled() throws InterruptedException {
		waitForLatch(taskCancelLatch, "Failed to wait for task to be cancelled");
	}
	
	public void assertPodcastListIsUpdated() throws InterruptedException {
		waitForLatch(taskFinishLatch, "Failed to wait for task to be finished");
	}

	protected void waitForLatch(CountDownLatch latch, String failureMessage)
			throws InterruptedException {
		if (latch.await(WAIT_TIMEOUT, TimeUnit.SECONDS)) {
			return;
		}
		Assert.fail(failureMessage);
	}
}