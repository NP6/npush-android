package com.np6.npush.internal.models.common;


public interface Completion<T> {
    void onComplete(Result<T> result);
}
