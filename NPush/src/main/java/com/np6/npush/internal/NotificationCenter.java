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

public class NotificationCenter {


    private final NotificationBuilder builder;

    private final Config config;

    NotificationManager notificationManager;

    public static NotificationCenter initialize(Context context, Config config) {
        if (Objects.isNull(context))
            throw new IllegalArgumentException();

        return new NotificationCenter(context, config);
    }

    private NotificationCenter(Context context, Config config) {
        this.builder = new NotificationBuilder(context, config);
        this.config = config;
        this.notificationManager = (NotificationManager)
                context.getSystemService(Context.NOTIFICATION_SERVICE);

    }

    public static Notification fromRemoteMessage(Map<String, String> remoteMessage) throws JsonProcessingException {
        return parse(remoteMessage);
    }

    public void submit(Notification notification, Completion<TrackingAction<String>> completion) {
        try  {

            TrackingAction<String> action = notification.getTracking().getImpressionAction();

            android.app.Notification builtNotification = this.build(notification);

            this.notificationManager.notify(new Random().nextInt(), builtNotification);

            completion.onComplete(
                    new Result.Success<>(action));
        } catch (Exception exception) {
            completion.onComplete(new Result.Error<>(exception));
        }
    }

    private android.app.Notification build(final Notification notification) throws Exception {

        this.builder
                .SetContent(notification.getRender().title, notification.getRender().body)
                .setDeeplink(notification.getMeta().getRedirection(), notification.getTracking())
                .setDismiss(notification.getTracking())
                .setIcon();

        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {

            final String channel =
                    Objects.isNull(notification.getMeta().getChannelId()) || notification.getMeta().getChannelId().isEmpty()
                            ? config.getDefaultChannel()
                            : notification.getMeta().getChannelId();

            this.builder.setChannel(channel);
        }

        return this.builder.build();
    }

    private static Notification parse(Map<String, String> remoteMessage) throws JsonProcessingException {

        Serializer serializer = new Serializer();

        Render render = serializer.deserialize(remoteMessage.get("render"), Render.class);
        Meta meta = serializer.deserialize(remoteMessage.get("meta"), Meta.class);
        Tracking tracking = serializer.deserialize(remoteMessage.get("tracking"), Tracking.class);

        return new Notification()
                .setMeta(meta)
                .setRender(render)
                .setTracking(tracking);
    }


    public static boolean isNotificationEnabled(Context context) {
        return NotificationManagerCompat.from(context).areNotificationsEnabled();
    }


    @RequiresApi(api = Build.VERSION_CODES.O)
    public boolean isNotificationChannelEnabled(
            Context context,
            @Nullable String channelId) {

        NotificationManager manager = (NotificationManager) context.getSystemService(Context.NOTIFICATION_SERVICE);
        NotificationChannel channel = manager.getNotificationChannel(channelId);
        if (channel != null) {
            return channel.getImportance() != NotificationManager.IMPORTANCE_NONE;
        } else {
            return true;
        }
    }

}
