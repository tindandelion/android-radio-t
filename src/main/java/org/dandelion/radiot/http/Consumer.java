package org.dandelion.radiot.http;

public interface Consumer<T> {
    void accept(T value);
}
