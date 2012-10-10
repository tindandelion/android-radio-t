package org.dandelion.radiot.live.util;

import java.util.List;

public class IterableValueProvider<T> implements ValueProvider<T> {
    private int nextPosition = 0;
    private List<T> values;

    public IterableValueProvider(List<T> values) {
        this.values = values;
    }

    @Override
    public T value() {
        T next = values.get(nextPosition);
        nextPosition = (nextPosition + 1) % values.size();
        return next;
    }
}
