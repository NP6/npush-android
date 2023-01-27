package com.np6.npush.internal.core.network.driver;

import com.np6.npush.internal.core.network.HttpClient;

import okhttp3.OkHttpClient;

public class Driver {

    private OkHttpClient client;

    private final String endpoint;

    public Driver(String endpoint) {
        this.client = HttpClient.Create();
        this.endpoint = endpoint;
    }

    public OkHttpClient getClient() {
        return client;
    }

    public String getEndpoint() {
        return endpoint;
    }
}
