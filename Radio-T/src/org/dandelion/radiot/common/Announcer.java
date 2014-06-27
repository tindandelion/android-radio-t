package org.dandelion.radiot.common;

import java.lang.reflect.InvocationHandler;
import java.lang.reflect.Method;
import java.lang.reflect.Proxy;

public class Announcer<T> {
    private final T proxy;
    private T target;

    public Announcer(Class<? extends T> listenerType) {
        proxy = listenerType.cast(Proxy.newProxyInstance(
                listenerType.getClassLoader(),
                new Class<?>[]{listenerType},
                new InvocationHandler() {
                    public Object invoke(Object aProxy, Method method, Object[] args) throws Throwable {
                        announce(method, args);
                        return null;
                    }
                }));
    }

    public void setTarget(T value) {
        target = value;
    }

    public T announce() {
        return proxy;
    }

    private void announce(Method method, Object[] args) {
        try {
            if (target != null) {
                method.invoke(target, args);
            }
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
    }
}
