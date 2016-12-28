package org.dandelion.radiot.http;

public class DisabledProvider<T> implements Provider<T> {
    @Override
    public T get() throws Exception {
        throw new Exception("Disabled");
    }

    @Override
    public void abort() {

    }
}
