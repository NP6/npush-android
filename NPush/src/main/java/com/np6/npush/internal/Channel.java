package com.np6.npush.internal;

import android.app.NotificationChannel;
import android.app.NotificationManager;
import android.content.Context;
import android.media.AudioAttributes;
import android.media.RingtoneManager;
import android.net.Uri;
import android.os.Build;

import androidx.annotation.RequiresApi;

import com.np6.npush.NPush;

@RequiresApi(Build.VERSION_CODES.O)
public class Channel {

    public static NotificationChannel createChannel(Context context, String channelId, String description) {

        NotificationChannel exist = getChannel(context, channelId);

        if (exist != null) {
            return exist;
        }

        NotificationManager notificationManager =
                (NotificationManager) context.getSystemService(Context.NOTIFICATION_SERVICE);

            NotificationChannel channel = new NotificationChannel(
                    NPush.Instance().getConfig().getDefaultChannel(),
                    channelId,
                    NotificationManager.IMPORTANCE_DEFAULT);

        Uri defaultSoundURI = RingtoneManager.getDefaultUri(RingtoneManager.TYPE_NOTIFICATION);

        AudioAttributes audioAttributes = new AudioAttributes
                .Builder()
                .setUsage(AudioAttributes.USAGE_NOTIFICATION)
                .build();

            channel.setDescription(description);
            channel.setVibrationPattern(new long[]{ 0, 1000, 500, 1000 });
            channel.setSound(defaultSoundURI, audioAttributes);
            notificationManager.createNotificationChannel(channel);

            return channel;

    }

    public static NotificationChannel getChannel(Context context, String channelId) {

        if (channelId == null || channelId.isEmpty()) {
            throw new IllegalArgumentException();
        }

        NotificationManager notificationManager =
                (NotificationManager) context.getSystemService(Context.NOTIFICATION_SERVICE);

        return notificationManager.getNotificationChannel(channelId);

    }

}
