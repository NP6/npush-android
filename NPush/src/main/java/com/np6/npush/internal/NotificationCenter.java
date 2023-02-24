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

    NotificationManagerCompat notificationManagerCompat;

    public static NotificationCenter initialize(Context context, Config config) {
        if (Objects.isNull(context))
            throw new IllegalArgumentException();

        if (Objects.isNull(config))
            throw new IllegalArgumentException();

        return new NotificationCenter(context, config);
    }

    private NotificationCenter(Context context, Config config) {
        this.builder = new NotificationBuilder(context, config);
        this.config = config;
        this.notificationManager = (NotificationManager)
                context.getSystemService(Context.NOTIFICATION_SERVICE);

        this.notificationManagerCompat = NotificationManagerCompat.from(context);
    }

    public static Notification fromRemoteMessage(Map<String, String> remoteMessage) throws JsonProcessingException {
        return parse(remoteMessage);
    }

    public void submit(Notification notification, Completion<TrackingAction<String>> completion) {
        try  {

            android.app.Notification builtNotification = this.build(notification);

            if (!isNotificationEnabled()) {
                TrackingAction<String> bounceAction = notification.getTracking().getGlobalOptoutAction();
                completion.onComplete(new Result.Success<>(bounceAction));
                return;
            }

            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
                if (isNotificationEnabled() && !isNotificationChannelEnabled(notification.getMeta().getChannelId())) {
                    TrackingAction<String> bounceAction = notification.getTracking().getChannelOptoutAction();
                    completion.onComplete(new Result.Success<>(bounceAction));
                    return;
                }
            }

            this.notificationManager.notify(new Random().nextInt(), builtNotification);

            TrackingAction<String> impressionAction = notification.getTracking().getImpressionAction();

            completion.onComplete(new Result.Success<>(impressionAction));
        } catch (Exception exception) {
            completion.onComplete(new Result.Error<>(exception));
        }
    }

    public android.app.Notification build(final Notification notification) throws Exception {

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
