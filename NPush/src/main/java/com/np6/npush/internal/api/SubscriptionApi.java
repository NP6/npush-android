package com.np6.npush.internal.api;


import android.os.Build;

import androidx.annotation.RequiresApi;

import com.np6.npush.internal.core.Constants;
import com.np6.npush.internal.core.Serializer;
import com.np6.npush.internal.core.concurrency.Concurrent;
import com.np6.npush.internal.core.network.HttpClient;
import com.np6.npush.internal.core.network.driver.Driver;
import com.np6.npush.internal.models.Subscription;
import com.np6.npush.internal.models.common.Completion;
import com.np6.npush.internal.models.common.Result;


import java.util.Objects;

import java9.util.concurrent.CompletableFuture;
import okhttp3.MediaType;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.RequestBody;
import okhttp3.Response;

public class SubscriptionApi {

    private Driver driver;

    private final String basePath;

    public SubscriptionApi(String identity, Driver driver) {
        String agency = identity.substring(0, 4);
        String customer = identity.substring(4, 7);
        this.basePath = Constants.WebServices.Subscription_Endpoint + agency + "/" + customer + "/subscriptions";
        this.driver = driver;
    }

    public static SubscriptionApi create(String identity) {

        if (Objects.isNull(identity) || identity.isEmpty()) {
            throw new IllegalArgumentException();
        }

        Driver driver = new Driver(HttpClient.Create());

        return new SubscriptionApi(identity, driver);
    }

    public void put(final Subscription subscription, final Completion<Subscription> completion) {
        Concurrent.Shared.executor.submit(() -> {

            try {
                Serializer serializer = new Serializer();
                String payload = serializer.serialize(subscription);

                RequestBody body = RequestBody.create(
                        payload,
                        MediaType.parse("application/json")
                );

                Request request = new Request.Builder()
                        .put(body)
                        .addHeader("Content-Type", "application/json")
                        .url(this.basePath)
                        .build();

                Response response = this.driver
                        .getClient()
                        .newCall(request)
                        .execute();

                if (!response.isSuccessful()) {
                    String message = "An error as occurred while creating subscription " +
                            response.code();

                    Concurrent.Shared.mainThreadHandler.post(() -> {
                        completion.onComplete(new Result.Error<>(new Exception(message)));
                    });
                    return;
                }
                Concurrent.Shared.mainThreadHandler.post(() -> {
                    completion.onComplete(new Result.Success<>(subscription));
                });

            } catch (Exception exception) {
                Concurrent.Shared.mainThreadHandler.post(() -> {
                    completion.onComplete(new Result.Error<>(exception));
                });
            }
        });
    }
}
