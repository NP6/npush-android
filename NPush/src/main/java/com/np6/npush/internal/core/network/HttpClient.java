package com.np6.npush.internal.core.network;

import com.np6.npush.internal.core.network.interceptor.UserAgentInterceptor;

import java.util.concurrent.TimeUnit;

import okhttp3.OkHttpClient;

public class HttpClient {

    public OkHttpClient HttpClient;

    private HttpClient(OkHttpClient httpClient) {
        HttpClient = httpClient;
    }

    public static OkHttpClient Create() {
        return new OkHttpClient.Builder()
                .addInterceptor(new UserAgentInterceptor())
                .connectTimeout(60000, TimeUnit.MILLISECONDS)
                .readTimeout(60000, TimeUnit.MILLISECONDS)
                .hostnameVerifier((hostname, session) -> true)
                .build();
    }

}
