package com.np6.npush;

import android.content.Context;

import com.np6.npush.internal.Installation;
import com.np6.npush.internal.NotificationCenter;
import com.np6.npush.internal.api.InteractionApi;
import com.np6.npush.internal.api.SubscriptionApi;
import com.np6.npush.internal.core.Logger;
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

public class NPush {

    private static NPush instance;

    private Config config;

    public DeeplinkInterceptor interceptor;

    private NPush() {
    }

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
            if (context == null) {
                throw new IllegalArgumentException("context must be specified");
            }

            if (config == null) {
                throw new IllegalArgumentException("config must be specified");
            }

            TokenProvider
                    .getProvider(context)
                    .getResultAsync((result -> {

                        if (result instanceof Result.Error)
                            Logger.Error(new Error<>(((Result.Error) result).exception));

                        if (result instanceof Result.Success)
                            TokenRepository
                                    .create(context)
                                    .Add(((Result.Success<String>) result).data);
                        Logger.Info(new Info<>("Token generate succeed "));
                    }));
        } catch (Exception exception) {
            Logger.Error(new Error<>(exception));
        }
    }

    public synchronized void setContact(Context context, Linked linked) {
        try {
            if (context == null)
                throw new IllegalArgumentException("context must be specified");


            if (linked == null)
                throw new IllegalArgumentException("linked must be specified");


            if (this.config == null)
                throw new IllegalArgumentException("config must be specified");


            Subscription subscription = Installation
                    .initialize(context, this.config)
                    .subscribe(linked)
                    .get();

            SubscriptionApi
                    .create(this.config.getIdentity())
                    .put(subscription)
                    .thenAccept(response -> {
                        if (response.isSuccessful()) {
                            Logger.Info(new Info<>("Subscription created successfully"));
                            return;
                        }
                        Logger.Error(new Error<>("Subscription creation failed with - status code  : " + response.code()));

                    }).exceptionally((throwable -> {
                        Logger.Error(new Error<>(throwable));
                        return null;
                    }));


        } catch (Exception exception) {
            Logger.Error(new Error<>(exception));
        }
    }

    public synchronized void handleNotification(Context context, Map<String, String> remoteData) {
        try {

            if (context == null)
                throw new IllegalArgumentException("context must be specified");

            if (remoteData == null)
                throw new IllegalArgumentException("remoteData must be specified");

            if (this.config == null)
                throw new IllegalArgumentException("config must be specified");

            Notification notification = NotificationCenter.fromRemoteMessage(remoteData);

            TrackingAction<String> action = NotificationCenter
                    .initialize(context, this.config)
                    .submit(notification)
                    .get();

            InteractionApi interactionApi = InteractionApi.create();

            interactionApi
                    .get(action.getRadical(), action.getValue())
                    .thenAccept(response -> {
                        if (response.isSuccessful()) {
                            Logger.Info(new Info<>("Action tracked successfully "));
                            return;
                        }
                        Logger.Error(new Error<>("Action tracking failed - status code : " + response.code()));

                    }).exceptionally((throwable -> {
                        Logger.Error(new Error<>(throwable));
                        return null;
                    }));

        } catch (Exception exception) {
            Logger.Error(new Error<>(exception));
        }
    }

}
