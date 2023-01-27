package com.np6.npush.internal.core.concurrency;

import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

public class Concurrent {

    public static class Shared {

        public final static ExecutorService executor = Executors.newSingleThreadExecutor();

    }
}
