package com.np6.npush.internal.provider;

import com.np6.npush.internal.models.common.Completion;

public interface Provider<T> {

    void getResultAsync(Completion<T> completion);

}
