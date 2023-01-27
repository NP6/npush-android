package com.np6.npush.internal.api;


import com.np6.npush.internal.core.Constants;
import com.np6.npush.internal.core.Serializer;
import com.np6.npush.internal.core.concurrency.Concurrent;
import com.np6.npush.internal.core.network.driver.Driver;
import com.np6.npush.internal.models.Subscription;
import com.np6.npush.internal.models.common.Completion;
import com.np6.npush.internal.models.common.Result;

import okhttp3.MediaType;
import okhttp3.Request;
import okhttp3.RequestBody;
import okhttp3.Response;

public class SubscriptionApi {

    private Driver driver;

    public SubscriptionApi(String identity) {
        String agency = identity.substring(0, 4);
        String customer = identity.substring(4, 7);
        String basePath = Constants.WebServices.Subscription_Endpoint + agency + "/" + customer + "/subscriptions";
        this.driver = new Driver(basePath);
    }

    public void put(Subscription subscription, Completion<Subscription> completion) {
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
                        .url(this.driver.getEndpoint())
                        .build();

                Response response = this.driver
                        .getClient()
                        .newCall(request)
                        .execute();

                if (!response.isSuccessful()) {
                    String message = "An error as occurred while creating subscription " +
                            response.code();
                    completion.onComplete(new Result.Error<>(new Exception(message)));
                    return;
                }

                completion.onComplete(new Result.Success<>(subscription));

            } catch (Exception exception) {
                completion.onComplete(new Result.Error<>(exception));
            }
        });
    }
}
