package com.np6.npush;

import android.content.Context;

import com.google.common.util.concurrent.ListenableFuture;
import com.google.common.util.concurrent.ListenableFutureTask;
import com.np6.npush.internal.Installation;
import com.np6.npush.internal.Interaction;
import com.np6.npush.internal.NotificationCenter;
import com.np6.npush.internal.api.SubscriptionApi;
import com.np6.npush.internal.core.Constants;
import com.np6.npush.internal.core.Logger;
import com.np6.npush.internal.core.concurrency.Concurrent;
import com.np6.npush.internal.models.DeeplinkInterceptor;
import com.np6.npush.internal.models.Subscription;
import com.np6.npush.internal.models.action.TrackingAction;
import com.np6.npush.internal.models.common.Result;
import com.np6.npush.internal.models.contact.Linked;
import com.np6.npush.internal.models.log.common.Error;
import com.np6.npush.internal.models.log.common.Info;
import com.np6.npush.internal.models.notification.Notification;
import com.np6.npush.internal.provider.TokenProvider;
import com.np6.npush.internal.repository.TokenRepository;

import java.util.Map;
import java.util.Objects;
import java.util.concurrent.CompletableFuture;

public class NPush {

    private static NPush instance;

    private Config config;

    public DeeplinkInterceptor interceptor;

    private NPush() { }

    public Config getConfig() {
        return config;
    }

    public void setInterceptor(DeeplinkInterceptor interceptor) {
        this.interceptor = interceptor;
    }

    public void setConfig(Config config) {
        this.config = config;
    }

    public static NPush Instance() {
        synchronized (NPush.class) {
            if (instance == null) {
                instance = new NPush();
            }
            return instance;
        }
    }

    public synchronized void initialize(Context context) {
        try {
            if (Objects.isNull(context)) {
                throw new IllegalArgumentException("context must be specified");
            }

            if (Objects.isNull(config)) {
                throw new IllegalArgumentException("config must be specified");
            }

            TokenProvider
                    .getProvider(context)
                    .getResultAsync((result -> {

                        if (result instanceof Result.Error)
                            Logger.Error(new Error<>(((Result.Error) result).exception));

                        if (result instanceof Result.Success) {
                            TokenRepository
                                    .Create(context)
                                    .Add(((Result.Success<String>) result).data);
                        }
                            Logger.Info(new Info<>("Token generate succeed "));
                    }));
        } catch (Exception exception) {
            Logger.Error(new Error<>(exception));
        }
    }

    public synchronized void setContact(Context context, Linked linked) {
        try {
            if (Objects.isNull(context))
                throw new IllegalArgumentException("context must be specified");


            if (Objects.isNull(linked))
                throw new IllegalArgumentException("linked must be specified");


            if (Objects.isNull(this.config))
                throw new IllegalArgumentException("config must be specified");

            Subscription subscription = Installation
                    .initialize(context, this.config)
                    .subscribe(linked);

            SubscriptionApi api = new SubscriptionApi(this.config.getIdentity());

            api.put(subscription, (result) -> {
                if (result instanceof Result.Error)
                    Logger.Error(new Error<>(((Result.Error) result).exception));

                if (result instanceof Result.Success) {
                    Logger.Info(new Info<>("Subscription created successfully"));
                }
            });

        } catch (Exception exception) {
            Logger.Error(new Error<>(exception));
        }
    }

    public synchronized void handleNotification(Context context, Map<String, String> remoteData) {
        try {

            if (Objects.isNull(context))
                throw new IllegalArgumentException("context must be specified");

            if (Objects.isNull(remoteData))
                throw new IllegalArgumentException("remoteData must be specified");

            if (Objects.isNull(this.config))
                throw new IllegalArgumentException("config must be specified");

            Notification notification = NotificationCenter.fromRemoteMessage(remoteData);


            NotificationCenter
                    .initialize(context, this.config)
                    .submit(notification, (result) -> {

                        if (result instanceof Result.Error)
                            Logger.Error(new Error<>(((Result.Error) result).exception));

                        if (result instanceof Result.Success) {

                            TrackingAction<String> action = ((Result.Success<TrackingAction<String>>) result).data;

                            Interaction.handle(action, (actionResult) -> {
                                if (actionResult instanceof Result.Error)
                                    Logger.Error(new Error<>(((Result.Error) result).exception));

                                if (actionResult instanceof Result.Success)
                                    Logger.Info(new Info<>("action tracked successfully"));

                            });
                        }
                    });
        } catch (Exception exception) {
            Logger.Error(new Error<>(exception));
        }
    }

}
