package org.dandelion.radiot.live.service;

public interface Lockable {
    void release();

    void acquire();
}
