package org.dandelion.radiot.http;

public interface Provider<T> {
    T get() throws Exception;
}
