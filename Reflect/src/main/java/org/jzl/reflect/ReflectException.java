package org.jzl.reflect;

public class ReflectException extends Exception {
    public ReflectException() {
    }

    public ReflectException(String s) {
        super(s);
    }

    public ReflectException(String s, Throwable throwable) {
        super(s, throwable);
    }

    public ReflectException(Throwable throwable) {
        super(throwable);
    }
}
