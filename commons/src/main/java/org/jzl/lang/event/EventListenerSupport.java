package org.jzl.lang.event;

import java.lang.reflect.InvocationHandler;
import java.lang.reflect.Method;
import java.lang.reflect.Proxy;
import java.util.List;
import java.util.Objects;
import java.util.concurrent.CopyOnWriteArrayList;

public class EventListenerSupport<T> {

    private T proxy;
    private List<T> listeners;

    EventListenerSupport(Class<T> listenerInterface, ClassLoader classLoader) {
        proxy = createProxy(listenerInterface, classLoader);
        listeners = new CopyOnWriteArrayList<>();
    }

    public EventListenerSupport<T> addListener(T listener, boolean allowDuplicate) {
        if (listener == null) {
            return this;
        }
        if (allowDuplicate) {
            listeners.add(listener);
        } else {
            if (!listeners.contains(listener)) {
                listeners.add(listener);
            }
        }
        return this;
    }

    public T proxy() {
        return proxy;
    }

    public int getListenerCount() {
        return listeners.size();
    }

    public EventListenerSupport<T> addListener(T listener) {
        addListener(listener, false);
        return this;
    }

    public EventListenerSupport<T> removeListener(T listener) {
        listeners.remove(listener);
        return this;
    }

    @SuppressWarnings("unchecked")
    private T createProxy(Class<T> listenerInterface, ClassLoader classLoader) {
        return (T) Proxy.newProxyInstance(classLoader, new Class[]{
                listenerInterface
        }, createInvocationHandler());
    }

    protected InvocationHandler createInvocationHandler() {
        return new ProxyInvocationHandler();
    }

    public static <T> EventListenerSupport<T> support(Class<T> listenerInterface, ClassLoader classLoader) {
        return new EventListenerSupport<>(listenerInterface, classLoader);
    }

    public static <T> EventListenerSupport<T> support(Class<T> listenerInterface) {
        Objects.requireNonNull(listenerInterface);
        return support(listenerInterface, listenerInterface.getClassLoader());
    }

    private class ProxyInvocationHandler implements InvocationHandler {

        @Override
        public Object invoke(Object proxy, Method method, Object[] args) throws Throwable {
            for (T listener : listeners) {
                method.invoke(listener, args);
            }
            return null;
        }
    }
}
