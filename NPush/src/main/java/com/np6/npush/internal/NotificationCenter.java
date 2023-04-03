package com.np6.npush.internal;

import android.app.NotificationChannel;
import android.app.NotificationManager;
import android.content.Context;
import android.os.Build;

import androidx.annotation.Nullable;
import androidx.annotation.RequiresApi;
import androidx.core.app.NotificationManagerCompat;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.np6.npush.Config;
import com.np6.npush.NPush;
import com.np6.npush.internal.core.Serializer;
import com.np6.npush.internal.models.action.Action;
import com.np6.npush.internal.models.action.TrackingAction;
import com.np6.npush.internal.models.common.Completion;
import com.np6.npush.internal.models.common.Result;
import com.np6.npush.internal.models.notification.Notification;
import com.np6.npush.internal.models.notification.input.Meta;
import com.np6.npush.internal.models.notification.input.Render;
import com.np6.npush.internal.models.notification.input.Tracking;

import java.util.Map;
import java.util.Objects;
import java.util.Random;

import java9.util.concurrent.CompletableFuture;

public class NotificationCenter {


    private final NotificationBuilder builder;

    private final Config config;

    NotificationManager notificationManager;

    NotificationManagerCompat notificationManagerCompat;

    public static NotificationCenter initialize(Context context, Config config) {
        if (context == null)
            throw new IllegalArgumentException();

        if (config == null)
            throw new IllegalArgumentException();

        NotificationBuilder notificationBuilder = NotificationBuilder.create(context, config);

        NotificationManager notificationManager = (NotificationManager)
                context.getSystemService(Context.NOTIFICATION_SERVICE);

        NotificationManagerCompat notificationManagerCompat = NotificationManagerCompat.from(context);

        return new NotificationCenter( config, notificationBuilder, notificationManager, notificationManagerCompat);
    }

    public NotificationCenter(
            Config config,
            NotificationBuilder notificationBuilder,
            NotificationManager notificationManager,
            NotificationManagerCompat notificationManagerCompat)
    {
        this.builder = notificationBuilder;
        this.config = config;
        this.notificationManager = notificationManager;
        this.notificationManagerCompat = notificationManagerCompat;
    }

    public static Notification fromRemoteMessage(Map<String, String> remoteMessage) throws JsonProcessingException {
        return parse(remoteMessage);
    }

    public CompletableFuture<TrackingAction<String>> submit(Notification notification) {
        try  {

            android.app.Notification builtNotification = this.build(notification);

            if (!isNotificationEnabled()) {
                TrackingAction<String> bounceAction = notification.getTracking().getGlobalOptoutAction();
                return CompletableFuture.completedFuture(bounceAction);
            }

            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
                if (isNotificationEnabled() && !isNotificationChannelEnabled(notification.getMeta().getChannelId())) {
                    TrackingAction<String> bounceAction = notification.getTracking().getChannelOptoutAction();
                    return CompletableFuture.completedFuture(bounceAction);
                }
            }

            this.notificationManager.notify(new Random().nextInt(), builtNotification);

            TrackingAction<String> impressionAction = notification.getTracking().getImpressionAction();

            return CompletableFuture.completedFuture(impressionAction);
        } catch (Exception exception) {
            return CompletableFuture.failedFuture(exception);
        }
    }

    public android.app.Notification build(Notification notification) throws Exception {

        this.builder
                .SetContent(notification.getRender().getTitle(), notification.getRender().getBody())
                .setDeeplink(notification.getMeta().getRedirection(), notification.getTracking())
                .setDismiss(notification.getTracking())
                .setIcon();

        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {

            final String channel =
                    notification.getMeta().getChannelId()  == null || notification.getMeta().getChannelId().isEmpty()
                            ? config.getDefaultChannel()
                            : notification.getMeta().getChannelId();

            this.builder.setChannel(channel);
        }

        return this.builder.build();
    }

    public static Notification parse(Map<String, String> remoteMessage) throws JsonProcessingException {

        Serializer serializer = new Serializer();

        Render render = serializer.deserialize(remoteMessage.get("render"), Render.class);
        Meta meta = serializer.deserialize(remoteMessage.get("meta"), Meta.class);
        Tracking tracking = serializer.deserialize(remoteMessage.get("tracking"), Tracking.class);

        return new Notification()
                .setMeta(meta)
                .setRender(render)
                .setTracking(tracking);
    }


    public boolean isNotificationEnabled() {
        return this.notificationManagerCompat.areNotificationsEnabled();
    }


    @RequiresApi(api = Build.VERSION_CODES.O)
    public boolean isNotificationChannelEnabled(@Nullable String channelId) {

        NotificationChannel channel = this.notificationManager.getNotificationChannel(channelId);

        if (channel == null) {
            return false;
        }

        return channel.getImportance() != NotificationManager.IMPORTANCE_NONE;
    }

}
