package com.np6.npush;

import static org.junit.Assert.assertEquals;
import static org.robolectric.Shadows.shadowOf;

import android.app.NotificationChannel;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.content.Context;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.media.AudioAttributes;
import android.media.RingtoneManager;
import android.net.Uri;

import androidx.core.app.NotificationCompat;
import androidx.test.core.app.ApplicationProvider;
import androidx.test.ext.junit.runners.AndroidJUnit4;

import com.np6.npush.internal.NotificationBuilder;
import com.np6.npush.internal.core.Constants;
import com.np6.npush.internal.models.notification.input.Tracking;

import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.ArgumentCaptor;
import org.mockito.Mockito;
import org.robolectric.RobolectricTestRunner;
import org.robolectric.annotation.Config;
import org.robolectric.shadow.api.Shadow;
import org.robolectric.shadows.ShadowPendingIntent;

import java.util.UUID;

@Config(sdk = {30})
@RunWith(AndroidJUnit4.class)
public class NotificationBuilderTest
{

    String defaultChannelName = "default_channel";

    Context context = ApplicationProvider.getApplicationContext();

    NotificationManager notificationManager;

    public static void createTestChannel(String channelId) {
        NotificationManager notificationManager =
                (NotificationManager) ApplicationProvider.getApplicationContext().getSystemService(Context.NOTIFICATION_SERVICE);

        NotificationChannel channel = new NotificationChannel(
                NPush.Instance().getConfig().getDefaultChannel(),
                channelId,
                NotificationManager.IMPORTANCE_DEFAULT);

        Uri defaultSoundURI = RingtoneManager.getDefaultUri(RingtoneManager.TYPE_NOTIFICATION);

        AudioAttributes audioAttributes = new AudioAttributes
                .Builder()
                .setUsage(AudioAttributes.USAGE_NOTIFICATION)
                .build();

        channel.setVibrationPattern(new long[]{ 0, 1000, 500, 1000 });
        channel.setSound(defaultSoundURI, audioAttributes);
        notificationManager.createNotificationChannel(channel);

    }

    public com.np6.npush.Config getValidConfig() {
        return new com.np6.npush.Config(UUID.randomUUID(), "MCOM032", "default_channel");
    }

    @Before
    public void setUp() {
        NPush.Instance().setConfig(getValidConfig());
        notificationManager = (NotificationManager)context.getSystemService(Context.NOTIFICATION_SERVICE);
    }


    @Test(expected = IllegalArgumentException.class)
    public void setNotificationContentWithEmptyTitle() {
        NotificationBuilder
                .create(context, getValidConfig())
                .SetContent("", "just a body");
    }

    @Test(expected = IllegalArgumentException.class)
    public void setNotificationContentWithEmptyBody() {
        NotificationBuilder
                .create(context, getValidConfig())
                .SetContent("just a title", "");
    }

    @Test
    public void setNotificationContentWithTitleAndBody() {

        String title = "valid_title";
        String content = "valid_content";

        NotificationCompat.Builder notificationCompatBuilderMock = Mockito.mock(NotificationCompat.Builder.class, Mockito.RETURNS_SELF);

        NotificationBuilder notificationBuilder = new NotificationBuilder(context, notificationCompatBuilderMock);

        notificationBuilder
                .SetContent(title, content);

        ArgumentCaptor<String> titleArgument = ArgumentCaptor.forClass(String.class);
        ArgumentCaptor<String> contentArgument = ArgumentCaptor.forClass(String.class);

        Mockito.verify(notificationCompatBuilderMock, Mockito.times(1)).setContentTitle(titleArgument.capture());
        assertEquals(title, titleArgument.getValue());

        Mockito.verify(notificationCompatBuilderMock, Mockito.times(1)).setContentTitle(contentArgument.capture());
        assertEquals(title, contentArgument.getValue());

    }

    @Test
    @Config(sdk = {30})
    public void setNotificationChannel() {
        createTestChannel(defaultChannelName);

        NotificationCompat.Builder notificationCompatBuilderMock = Mockito.mock(NotificationCompat.Builder.class, Mockito.RETURNS_SELF);

        NotificationBuilder notificationBuilder = new NotificationBuilder(context, notificationCompatBuilderMock);

        notificationBuilder.setChannel(defaultChannelName);

        ArgumentCaptor<String> channelArgument = ArgumentCaptor.forClass(String.class);

        Mockito.verify(notificationCompatBuilderMock, Mockito.times(1)).setChannelId(channelArgument.capture());
        assertEquals(defaultChannelName, channelArgument.getValue());

    }

    @Test
    @Config(sdk = {25})
    public void testNotificationChannelNotSet() {

        NotificationCompat.Builder notificationCompatBuilderMock = Mockito.mock(NotificationCompat.Builder.class, Mockito.RETURNS_SELF);

        NotificationBuilder notificationBuilder = new NotificationBuilder(context, notificationCompatBuilderMock);

        notificationBuilder.setChannel(defaultChannelName);

        ArgumentCaptor<String> channelArgument = ArgumentCaptor.forClass(String.class);

        Mockito.verify(notificationCompatBuilderMock, Mockito.times(0)).setChannelId(channelArgument.capture());

    }

    @Test
    public void testSetNotificationIcon() throws PackageManager.NameNotFoundException {

        NotificationCompat.Builder notificationCompatBuilderMock = Mockito.mock(NotificationCompat.Builder.class, Mockito.RETURNS_SELF);

        NotificationBuilder notificationBuilder = new NotificationBuilder(context, notificationCompatBuilderMock);

        notificationBuilder.setIcon();

        ArgumentCaptor<Integer> channelArgument = ArgumentCaptor.forClass(int.class);

        Mockito.verify(notificationCompatBuilderMock, Mockito.times(1)).setSmallIcon(channelArgument.capture());
    }

    @Test
    public void testSetNotificationDeeplinkIntent() {

        String deeplink = "myapp://test/product";
        String radical = "https://tracking.np6.com";
        String redirection = "q5bVk53ZsGrIPMW681JYfZ5Fgvo_LtDKZXpZmAmgPxw80imc08vrCSSfoaKiWLQe4dCBwG8r6t6rWhoRjnLkMgLq";

        Tracking trackingMock = Mockito.mock(Tracking.class);

        Mockito.when(trackingMock.getRedirection()).thenReturn(redirection);
        Mockito.when(trackingMock.getRadical()).thenReturn(radical);

        NotificationCompat.Builder notificationCompatBuilderMock = Mockito.mock(NotificationCompat.Builder.class, Mockito.RETURNS_SELF);

        NotificationBuilder notificationBuilder = new NotificationBuilder(context, notificationCompatBuilderMock);

        notificationBuilder.setDeeplink(deeplink, trackingMock);

        Mockito.verify(trackingMock, Mockito.times(1)).getRadical();
        Mockito.verify(trackingMock, Mockito.times(1)).getRedirection();

        ArgumentCaptor<PendingIntent> intentArgument = ArgumentCaptor.forClass(PendingIntent.class);

        Mockito.verify(notificationCompatBuilderMock, Mockito.times(1)).setContentIntent(intentArgument.capture());

        ShadowPendingIntent intent = shadowOf(intentArgument.getValue());

        assertEquals(redirection, intent.getSavedIntent().getStringExtra(Constants.Extra.BUNDLE_KEY_REDIRECTION_KEY));
        assertEquals(radical, intent.getSavedIntent().getStringExtra(Constants.Extra.BUNDLE_RADICAL_KEY));
        assertEquals(deeplink, intent.getSavedIntent().getStringExtra(Constants.Extra.BUNDLE_DEEPLINK_KEY));

    }

    @Test
    public void testSetNotificationDismissIntent() throws Exception {

        String radical = "https://tracking.np6.com";
        String dismiss = "q5bVk53ZsGrIPMW681JYfZ5Fgvo_LtDKZXpZmAmgPxw80imc08vrCSSfoaKiWLQe4dCBwG8r6t6rWhoRjnLkMgLq";


        Tracking trackingMock = Mockito.mock(Tracking.class);

        Mockito.when(trackingMock.getDismiss()).thenReturn(dismiss);
        Mockito.when(trackingMock.getRadical()).thenReturn(radical);


        NotificationCompat.Builder notificationCompatBuilderMock = Mockito.mock(NotificationCompat.Builder.class, Mockito.RETURNS_SELF);

        NotificationBuilder notificationBuilder = new NotificationBuilder(context, notificationCompatBuilderMock);

        notificationBuilder.setDismiss(trackingMock);


        Mockito.verify(trackingMock, Mockito.times(1)).getRadical();
        Mockito.verify(trackingMock, Mockito.times(1)).getDismiss();


        ArgumentCaptor<PendingIntent> intentArgument = ArgumentCaptor.forClass(PendingIntent.class);

        Mockito.verify(notificationCompatBuilderMock, Mockito.times(1)).setDeleteIntent(intentArgument.capture());

        ShadowPendingIntent intent = shadowOf(intentArgument.getValue());

        assertEquals(dismiss, intent.getSavedIntent().getStringExtra(Constants.Extra.BUNDLE_KEY_DISMISS_KEY));
        assertEquals(radical, intent.getSavedIntent().getStringExtra(Constants.Extra.BUNDLE_RADICAL_KEY));

    }
}
