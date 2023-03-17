package com.np6.npush.internal.api;


import androidx.annotation.NonNull;

import com.np6.npush.internal.core.Constants;
import com.np6.npush.internal.core.Serializer;
import com.np6.npush.internal.core.network.HttpClient;
import com.np6.npush.internal.core.network.driver.Driver;


import java.io.IOException;
import java.util.Objects;

import java9.util.concurrent.CompletableFuture;
import okhttp3.Call;
import okhttp3.Callback;
import okhttp3.MediaType;
import okhttp3.Request;
import okhttp3.RequestBody;
import okhttp3.Response;

public class TelemetryApi {

    private final Driver driver;

    private final String basePath;

    public TelemetryApi(String identity, Driver driver) {
        String agency = identity.substring(0, 4);
        String customer = identity.substring(4, 7);
        this.basePath = Constants.WebServices.Telemetry_Endpoint + agency + "/" + customer + "/telemetry";
        this.driver = driver;
    }

    public static TelemetryApi create(String identity) {

        if (identity == null || identity.isEmpty()) {
            throw new IllegalArgumentException();
        }

        Driver driver = new Driver(HttpClient.Create());

        return new TelemetryApi(identity, driver);
    }

    public CompletableFuture<Response> log(final com.np6.npush.internal.models.log.Log log) {

        CompletableFuture<Response> future = new CompletableFuture<>();

        try {
            Serializer serializer = new Serializer();
            String payload = serializer.serialize(log);

            RequestBody body = RequestBody.create(
                    payload,
                    MediaType.parse("application/json")
            );

            Request request = new Request.Builder()
                    .put(body)
                    .addHeader("Content-Type", "application/json")
                    .url(this.basePath)
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
