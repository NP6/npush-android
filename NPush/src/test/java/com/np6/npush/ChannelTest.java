package com.np6.npush;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotEquals;

import android.app.NotificationChannel;
import android.app.NotificationManager;
import android.content.Context;

import androidx.test.core.app.ApplicationProvider;
import androidx.test.ext.junit.runners.AndroidJUnit4;

import com.np6.npush.NPush;
import com.np6.npush.internal.Channel;

import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.robolectric.RobolectricTestRunner;
import org.robolectric.annotation.Config;

import java.util.UUID;

@RunWith(AndroidJUnit4.class)
public class ChannelTest {
    private static Context context;

    private static String channelId, channelDescription, defaultChannelId, actualDefaultChannelId;
    private static NotificationChannel actualChannel, actualDefaultChannel;

    @Before
    public void init() {
        com.np6.npush.Config config = new com.np6.npush.Config(
                UUID.fromString("12345678-1234-1234-1234-123456789abc"),
                "IDEN012",
                "default channel test name",
                false
        );
        NPush.Instance().setConfig(config);
        defaultChannelId = NPush.Instance().getConfig().getDefaultChannel();
        context = ApplicationProvider.getApplicationContext();
        channelId = "Channel id";
        channelDescription = "Channel description";
        actualChannel = new NotificationChannel(channelId,
                 "ChannelName", 3);
        actualDefaultChannelId = NPush.Instance().getConfig().getDefaultChannel();
        NotificationManager notificationManager =
                (NotificationManager) context.getSystemService(Context.NOTIFICATION_SERVICE);
        notificationManager.createNotificationChannel(actualChannel);
    }

    @Test
    public void CheckExistingChannel() {
        NotificationChannel expectedChannel = Channel.getChannel(context, channelId);
        assertEquals(expectedChannel,actualChannel);
    }

    @Test
    public void CheckNonExistingChannel() {
        NotificationChannel expectedChannel = Channel.getChannel(context,"fakeChannelId");
        assertNotEquals(expectedChannel,actualChannel);
    }

}
