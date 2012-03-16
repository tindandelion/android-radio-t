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
            Assert.assertTrue(waitTimeoutExpiredMessage(WAIT_TIMEOUT_SECONDS),
                    latch.await(WAIT_TIMEOUT_SECONDS, TimeUnit.SECONDS));
        } catch (InterruptedException e) {
            throw new RuntimeException(e);
        }
    }

    private String waitTimeoutExpiredMessage(long seconds) {
        return String.format("Value was not set in %d seconds", seconds);
    }

    private void signalValueSet() {
        latch.countDown();
    }
}
