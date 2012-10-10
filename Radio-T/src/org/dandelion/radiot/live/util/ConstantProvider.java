package org.dandelion.radiot.live.util;

public class ConstantProvider<T> implements ValueProvider<T> {

    private T value;

    public ConstantProvider(T value) {
        this.value = value;
    }

    @Override
    public T value() {
        return value;
    }
}
