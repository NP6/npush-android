package com.np6.npush.internal.provider;

import java9.util.concurrent.CompletableFuture;

public interface Provider<T> {

    CompletableFuture<T> getResultAsync();

}
