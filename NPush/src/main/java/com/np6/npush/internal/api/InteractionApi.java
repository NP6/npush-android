package com.np6.npush.internal.api;

import com.np6.npush.internal.core.concurrency.Concurrent;
import com.np6.npush.internal.core.network.driver.Driver;
import com.np6.npush.internal.models.common.Completion;
import com.np6.npush.internal.models.common.Result;

import java.util.Objects;

import okhttp3.Request;
import okhttp3.Response;

public class InteractionApi {


    private Driver driver;

    public InteractionApi(Driver driver) {
        this.driver = driver;
    }


    public void get(String radical, String resource, Completion<Object> completion) {
        Concurrent.Shared.executor.submit(() -> {

            try {
                Request request = new Request.Builder()
                        .addHeader("Content-Type", "application/json")
                        .url(radical + resource)
                        .build();

                Response response = this.driver
                        .getClient()
                        .newCall(request)
                        .execute();

                if (!response.isSuccessful()) {
                    String message = "HTTP request failed - Status code :  " +
                            response.code()  + " - response : " + Objects.requireNonNull(response.body()).string();
                    throw new Exception(message);
                }

                completion.onComplete(new Result.Success<>(new Object()));

            } catch (Exception exception) {
                completion.onComplete(new Result.Error<>(exception));
            }
        });
    }
}
