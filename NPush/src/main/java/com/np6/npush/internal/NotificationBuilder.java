package com.np6.npush.internal;

import android.app.Notification;
import android.app.PendingIntent;
import android.content.Context;
import android.content.Intent;
import android.content.pm.ApplicationInfo;
import android.content.pm.PackageManager;
import android.os.Build;

import androidx.core.app.NotificationCompat;

import com.np6.npush.Config;
import com.np6.npush.NPush;
import com.np6.npush.internal.core.Constants;
import com.np6.npush.internal.core.IntentHelper;
import com.np6.npush.internal.models.notification.input.Tracking;
import com.np6.npush.internal.system.ActionBroadcastReceiver;
import com.np6.npush.internal.system.TransparentActivity;

import java.util.HashMap;
import java.util.Map;
import java.util.Objects;

public class NotificationBuilder {

    private Context context;

    NotificationCompat.Builder nativeBuilder;

    public static NotificationBuilder create(Context context, Config config) {

        NotificationCompat.Builder nativeBuilder = new NotificationCompat.Builder(context, config.getDefaultChannel());

        return new NotificationBuilder(context, nativeBuilder);
    }

    public NotificationBuilder(Context context, NotificationCompat.Builder nativeBuilder) {
        this.context = context;
        this.nativeBuilder = nativeBuilder;
        this.nativeBuilder.setAutoCancel(true);
    }

    public NotificationBuilder SetContent(String title, String body) {

        if (Objects.isNull(title) || title.isEmpty()) {
            throw new IllegalArgumentException();
        }

        if (Objects.isNull(body) || body.isEmpty()) {
            throw new IllegalArgumentException();
        }

        this.nativeBuilder
                .setContentText(body)
                .setContentTitle(title)
                .setStyle(new NotificationCompat.BigTextStyle()
                        .bigText(body)
                        .setBigContentTitle(title));
        return this;
    }

    public NotificationBuilder setIcon() throws PackageManager.NameNotFoundException {
        ApplicationInfo applicationInfo = context.getPackageManager().getApplicationInfo(context.getPackageName(), 0);
        this.nativeBuilder.setSmallIcon(applicationInfo.icon);
        return this;
    }

    public NotificationBuilder setChannel(String channelId) {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
            nativeBuilder.setChannelId(Channel.getChannelOrDefault(context, channelId));
        }
        return this;
    }

    public NotificationBuilder setDeeplink(String deeplink, Tracking tracking) {
        Map<String, String> data = new HashMap<>();

        data.put(Constants.Extra.BUNDLE_KEY_REDIRECTION_KEY, tracking.getRedirection());
        data.put(Constants.Extra.BUNDLE_DEEPLINK_KEY, deeplink);
        data.put(Constants.Extra.BUNDLE_RADICAL_KEY, tracking.getRadical());

        Intent launchIntent = IntentHelper.CreateIntent(context, TransparentActivity.class, data);

        PendingIntent deeplinkPendingIntent = IntentHelper.CreatePendingActivityIntent(context, launchIntent);

        this.nativeBuilder.setContentIntent(deeplinkPendingIntent);

        return this;
    }

    public NotificationBuilder setDismiss(Tracking tracking) throws Exception {

        Map<String, String> data = new HashMap<>();
        data.put(Constants.Extra.BUNDLE_KEY_DISMISS_KEY, tracking.getDismiss());
        data.put(Constants.Extra.BUNDLE_RADICAL_KEY, tracking.getRadical());

        Intent dismissIntent = IntentHelper.CreateIntent(context, Constants.Intent.INTENT_DISMISS_INTENT, ActionBroadcastReceiver.class, data);
        PendingIntent dismissPendingIntent = IntentHelper.CreatePendingBroadcastIntent(context, dismissIntent);

        this.nativeBuilder.setDeleteIntent(dismissPendingIntent);

        return this;
    }

    public Notification build() {
        return this.nativeBuilder.build();
    }

}