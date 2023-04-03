package com.np6.npush;

import android.content.Context;

import com.np6.npush.internal.Installation;
import com.np6.npush.internal.NotificationCenter;
import com.np6.npush.internal.api.InteractionApi;
import com.np6.npush.internal.core.logger.Console;
import com.np6.npush.internal.core.logger.Logger;
import com.np6.npush.internal.models.DeeplinkInterceptor;
import com.np6.npush.internal.models.action.TrackingAction;
import com.np6.npush.internal.models.contact.Linked;
import com.np6.npush.internal.models.log.common.Error;
import com.np6.npush.internal.models.log.common.Info;
import com.np6.npush.internal.models.notification.Notification;
import com.np6.npush.internal.provider.TokenProvider;
import com.np6.npush.internal.repository.TokenRepository;

import java.util.Map;

import java9.util.concurrent.CompletableFuture;

public class NPush {

    public static Logger console = new Console();

    private static NPush instance;

    private Config config;

    public DeeplinkInterceptor interceptor;

    private NPush() {}

    public Config getConfig() {
        return config;
    }

    public void setInterceptor(DeeplinkInterceptor interceptor) {
        this.interceptor = interceptor;
    }

    public synchronized void setConfig(Config config) {
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
                    .getResultAsync()
                    .thenAccept(token -> TokenRepository.create(context).Add(token))
                    .exceptionally((throwable -> {
                        console.error(new Error<>(throwable));
                        return null;
                    }));

        } catch (Exception exception) {
            console.error(new Error<>(exception));
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


            Installation
                    .create(context, this.config)
                    .subscribe(linked)
                    .thenAccept(response -> {
                        if (response.isSuccessful()) {
                            console.info(new Info<>("Subscription created successfully"));
                            return;
                        }
                        console.error(new Error<>(new Exception("Subscription creation failed with http status code " + response.code())));

                    }).exceptionally((throwable -> {
                        console.error(new Error<>(throwable));
                        return null;
                    }));


        } catch (Exception exception) {
            console.error(new Error<>(exception));
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

            NotificationCenter notificationCenter = NotificationCenter.initialize(context, this.config);

            CompletableFuture<TrackingAction<String>> future = notificationCenter.submit(notification);

            TrackingAction<String> action = future.get();

            InteractionApi
                    .create()
                    .get(action.getRadical(), action.getValue())
                    .thenAccept(response -> {
                        if (response.isSuccessful()) {
                            console.info(new Info<>("Action tracked successfully "));
                            return;
                        }
                        console.error(new Error<>(new Exception("Action tracking failed with status code : " + response.code())));

                    }).exceptionally((throwable -> {
                        console.error(new Error<>(throwable));
                        return null;
                    }));

        } catch (Exception exception) {
            console.error(new Error<>(exception));
        }
    }

}
