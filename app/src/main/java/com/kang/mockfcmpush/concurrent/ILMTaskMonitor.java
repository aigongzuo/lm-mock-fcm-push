package com.kang.mockfcmpush.concurrent;

public interface ILMTaskMonitor<T> {
    void onStart(T var1);

    void onEnd(T var1);

    void onError(T var1, Throwable throwable);

    void onCancel(T var1);
}
