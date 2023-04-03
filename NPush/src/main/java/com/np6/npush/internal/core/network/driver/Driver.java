package com.np6.npush.internal.core.network.driver;

import com.np6.npush.internal.core.network.HttpClient;

import okhttp3.OkHttpClient;

public class Driver {

    private final OkHttpClient client;

    public Driver(OkHttpClient client) {
        this.client = client;
    }

    public OkHttpClient getClient() {
        return client;
    }
}
