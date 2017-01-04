package org.dandelion.radiot.live.core;

public interface Timeout {
    void reset();
    void set(int milliseconds);
}
