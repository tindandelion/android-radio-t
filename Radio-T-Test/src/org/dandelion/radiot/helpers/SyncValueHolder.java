package org.dandelion.radiot.helpers;

import junit.framework.Assert;

import java.util.concurrent.CountDownLatch;
import java.util.concurrent.TimeUnit;

public class SyncValueHolder<T> {
    private static final long WAIT_TIMEOUT_SECONDS = 10;
    private CountDownLatch latch = new CountDownLatch(1);
    private T value;

    public void setValue(T value) {
        this.value = value;
        signalValueSet();
    }

    public T getValue() {
        waitForValueToBeSet();
        return value;
    }

    private void waitForValueToBeSet() {
        try {
            latch.await(WAIT_TIMEOUT_SECONDS, TimeUnit.SECONDS);
        } catch (InterruptedException e) {
            Assert.fail(String.format("Failed to wait for value in %d seconds", WAIT_TIMEOUT_SECONDS));
        }
    }

    private void signalValueSet() {
        latch.countDown();
    }
}
