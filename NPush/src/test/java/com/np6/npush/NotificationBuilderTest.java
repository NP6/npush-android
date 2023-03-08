package com.np6.npush;

import static org.junit.Assert.assertEquals;
<<<<<<< HEAD
import static org.robolectric.Shadows.shadowOf;

import android.app.NotificationChannel;
=======

>>>>>>> fc3dd14e326373b3d74a7502d60ed1b3fc380deb
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

import com.fasterxml.jackson.core.JsonProcessingException;
import com.np6.npush.internal.NotificationBuilder;
import com.np6.npush.internal.NotificationCenter;
import com.np6.npush.internal.models.notification.Notification;

import org.junit.Before;
<<<<<<< HEAD
=======
import org.junit.Ignore;
>>>>>>> fc3dd14e326373b3d74a7502d60ed1b3fc380deb
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.ArgumentCaptor;
import org.mockito.Mockito;
import org.robolectric.RobolectricTestRunner;
import org.robolectric.annotation.Config;
import org.robolectric.shadow.api.Shadow;
import org.robolectric.shadows.ShadowPendingIntent;

import java.util.HashMap;
import java.util.Map;
import java.util.UUID;

@Config(sdk = {30})
<<<<<<< HEAD
@RunWith(AndroidJUnit4.class)
public class NotificationBuilderTest
{

    String defaultChannelName = "default_channel";

    Context context = ApplicationProvider.getApplicationContext();

    NotificationManager notificationManager;
=======
@RunWith(RobolectricTestRunner.class)
public class NotificationBuilderTest {

    Context context = ApplicationProvider.getApplicationContext();
    NotificationBuilder notificationBuilder;
    Notification notification;
>>>>>>> fc3dd14e326373b3d74a7502d60ed1b3fc380deb

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
    public void setUp() throws JsonProcessingException {
        NPush.Instance().setConfig(getValidConfig());
        notificationBuilder = new NotificationBuilder(context, getValidConfig());
        notification = NotificationCenter.fromRemoteMessage(getValidRemoteMessage());
    }

    public Map<String, String> getValidRemoteMessage() {

        Map<String, String> remoteMessage = new HashMap<>();

        remoteMessage.put("meta", "{ " +
                "\"notification\": 190, " +
                "\"version\": \"5e13d3e3-acbd-4256-9196-781369c2d143\", " +
                "\"application\": \"c5d6993f-7e81-4039-8755-5b82694bf473\", " +
                "\"stamp\": { " +
                "\"time\": 1677075960784, " +
                "\"id\": \"75bbf10e-b53d-4a70-93b1-605ad429537a\", " +
                "\"thread\": \"cee44d8d-ad28-434f-bfa2-52074c146109\", " +
                "\"set\": \"c74f35a4-3641-44e0-839a-60d6e14d68d3\" " +
                "}, " +
                "\"channel\": \"plop\", " +
                "\"redirection\": \"app://npush/product3\" " +
                "}");

        remoteMessage.put("render", "{ \"title\": \"title test\", \"body\": \"body test\" }");

        remoteMessage.put("tracking", "{ " +
                "\"radical\": \"https://tracking.dev.np6.com/hit/MCOM/032/gz/eXXS70LQkWREN8MQmEdwi7MkJJWsLJcHG-h1Ho296ydkZ0l7f-okL-f60G0s-_LaTAH3d938ga3fafOkGyM-nM7tGGjfXaYQBxXMS4NTTvgMxzDaEcfkf4aznw6lFxebZhoknKhuygNOzhEklqwoUFj9M0WXegDR3oWHK6QbGkyzFJznv47fCB5rMu63PJTgeVM58QLwZbMW3A30nM7zq6kZBCgSFi5byAak41hGyDK397iZOdt91X2vVfIZUJlwNwZiX5WSvWrztvSngIex-Y9nyL_o2tC0GDpCEZYhW8SNKumYSS7e7oN36qRbeErPQKp0qx6NyKxQEvUdJwB31JDgwC0ugKGKRoLxqjg4qH8Im-n0hdJG24mTPfyMvvWXEBQm_ZPOdUC8ej9rC7DyyVqkKTnCeYV1b2zzEyn_RO_g-uvamn0uTmsP1hN1KbxvcmH7RHX7yYJwjSK7-j5DC6sGf70aQrk90kjsjpCNeYFz6fGVFev8g0NJrxfcGSGSgHrS7yU8u-_zs58e_TXK23oGXcfI_Q8TmhSoyStu-ycySvfX6Y9Oq8oCk1jspofa4N5I/link/\", " +
                "\"impression\": \"mBgK2q_QgU8X_fg_PP2NWzxAt6gU0bLVQmBhZ9xnk6Z-qINfy5oDPaQFHzBxMV5SY_S-vYILgRcIfZj7TQj495sJIglj\", " +
                "\"redirection\": \"fdSrGlqMubuRK_DkREvnd3oeH6bTzVO8AJyuuoeESSKDIxgsxw83C4fi0t6rsiqC0i7yJg3sNC4UotQSHFmqzde1vYqcu4_CbLwX4cRU9OHcG76FGjSZutV1GoreBkp2hyUIZVJhjA\", " +
                "\"dismiss\": \"q5bVk53ZsGrIPMW681JYfZ5Fgvo_LtDKZXpZmAmgPxw80imc08vrCSSfoaKiWLQe4dCBwG8r6t6rWhoRjnLkMgLq\", " +
                "\"optout\": { " +
                "\"global\": \"lvCUOzBF3NmCv98FI24iRCUoo2BPbjmgq_iRsFPpOfPOVwYO58YSFsUK9neUVDoGTb_WEdw4kuMX3UpAoFB2Yjc\", " +
                "\"channel\": \"2vNX29icDGokLLNOBZZwmgZhE0D8jkv4bCzBW34PKuzG9O6G-U4Ingb49RwesFtDMEHSPgC5mNYYpFmG0qeW8eBbde6QlflExVejbpy16QVK\" " +
                "} " +
                "}");
        return remoteMessage;
    }

    @Test(expected = IllegalArgumentException.class)
    public void TitleIsNull() {
        notificationBuilder.SetContent(null, notification.getRender().body);
    }

<<<<<<< HEAD

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
=======
    @Test(expected = IllegalArgumentException.class)
    public void BodyIsNull() {
        notificationBuilder.SetContent(notification.getRender().title, null);
    }

    @Test
    public void SetContent() {
        notificationBuilder.SetContent(notification.getRender().title, notification.getRender().body);
        assertEquals("title test", notificationBuilder.build().extras.get("android.title"));
        assertEquals("body test", notificationBuilder.build().extras.get("android.text"));
    }

    @Test
    public void SetChannel() {
        notificationBuilder.setChannel(notification.getMeta().getChannelId());
        assertEquals("default_channel", notificationBuilder.build().getChannelId());
    }

    @Ignore
    @Test
    public void SetDeeplink(){
        notificationBuilder.setDeeplink(notification.getMeta().getRedirection(), notification.getTracking());
    }

    @Ignore
    @Test
    public void SetDismiss() throws Exception {
        notificationBuilder.setDismiss(notification.getTracking());
    }

>>>>>>> fc3dd14e326373b3d74a7502d60ed1b3fc380deb
}
