package com.pashikhmin.ismobileapp.network.loadTask;

public class LoadTaskResult<T> {
    T res;
    Throwable exc;

    public LoadTaskResult(T result) {
        res = result;
    }

    public LoadTaskResult(Throwable exc) {
        this.exc = exc;
    }

    public T getResult() {
        return res;
    }

    public Throwable getError() {
        return exc;
    }

    public boolean successful() {
        return exc == null;
    }
}
