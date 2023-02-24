package com.np6.npush.internal.core.concurrency;

import android.os.Handler;
import android.os.Looper;

import androidx.core.os.HandlerCompat;

import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

public class Concurrent {

    public static class Shared {

        public static ExecutorService executor = Executors.newSingleThreadExecutor();

        public final static Handler mainThreadHandler = HandlerCompat.createAsync(Looper.getMainLooper());

    }
}
