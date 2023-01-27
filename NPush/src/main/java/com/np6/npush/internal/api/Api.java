package com.np6.npush.internal.api;

import com.np6.npush.internal.models.common.Completion;

public interface Api {

    <U, T> void put(U value, Completion<T> completion);


    <U, T> void get(U value, Completion<T> completion);
}
