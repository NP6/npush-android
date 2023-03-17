package com.np6.npush.internal.api;

import androidx.annotation.NonNull;

import com.np6.npush.internal.core.Serializer;
import com.np6.npush.internal.core.concurrency.Concurrent;
import com.np6.npush.internal.core.network.HttpClient;
import com.np6.npush.internal.core.network.driver.Driver;
import com.np6.npush.internal.models.common.Completion;
import com.np6.npush.internal.models.common.Result;

import java.io.IOException;
import java.util.Objects;

import java9.util.concurrent.CompletableFuture;
import okhttp3.Call;
import okhttp3.Callback;
import okhttp3.MediaType;
import okhttp3.Request;
import okhttp3.RequestBody;
import okhttp3.Response;

public class InteractionApi {


    private final Driver driver;

    public InteractionApi(Driver driver) {
        this.driver = driver;
    }

    public static InteractionApi create() {

        Driver driver = new Driver(HttpClient.Create());

        return new InteractionApi(driver);
    }


    public CompletableFuture<Response> get(String radical, String resource) {
        CompletableFuture<Response> future = new CompletableFuture<>();

        try {
            Request request = new Request.Builder()
                    .url(radical + resource)
                    .build();

            this.driver
                    .getClient()
                    .newCall(request)
                    .enqueue(new Callback() {
                        @Override
                        public void onFailure(@NonNull Call call, @NonNull IOException e) {
                            future.completeExceptionally(e);
                        }

                        @Override
                        public void onResponse(@NonNull Call call, @NonNull Response response) throws IOException {
                            Objects.requireNonNull(response.body()).close();
                            future.complete(response);
                        }
                    });
        } catch (Exception exception) {
            future.completeExceptionally(exception);
        }
        return future;
    }
}
