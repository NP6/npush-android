package com.np6.npush;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertTrue;
import static org.mockito.ArgumentMatchers.eq;
import static org.mockito.Mockito.RETURNS_SELF;
import static org.mockito.Mockito.verify;

import android.app.NotificationChannel;
import android.app.NotificationManager;
import android.content.Context;
import android.content.Intent;
import android.service.notification.StatusBarNotification;

import androidx.core.app.NotificationCompat;
import androidx.core.app.NotificationManagerCompat;
import androidx.test.core.app.ApplicationProvider;
import androidx.test.ext.junit.runners.AndroidJUnit4;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.np6.npush.internal.NotificationBuilder;
import com.np6.npush.internal.NotificationCenter;
import com.np6.npush.internal.core.Serializer;
import com.np6.npush.internal.models.action.TrackingAction;
import com.np6.npush.internal.models.notification.Notification;
import com.np6.npush.internal.models.notification.input.Meta;
import com.np6.npush.internal.models.notification.input.Render;
import com.np6.npush.internal.models.notification.input.Tracking;

import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.ArgumentCaptor;
import org.mockito.ArgumentMatchers;
import org.mockito.Mockito;
import org.robolectric.RobolectricTestRunner;
import org.robolectric.annotation.Config;

import java.util.HashMap;
import java.util.Map;
import java.util.UUID;

@Config(sdk = {30})
@RunWith(AndroidJUnit4.class)
public class NotificationCenterTest {

    Context context = ApplicationProvider.getApplicationContext();
    NotificationManager notificationManager;


    @Before
    public void setUp() {
        NPush.Instance().setConfig(getValidConfig());
        notificationManager = (NotificationManager)context.getSystemService(Context.NOTIFICATION_SERVICE);
    }


    public Map<String, String> getInvalidRemoteMessage() {
        Map<String, String> remoteMessage = new HashMap<>();

        remoteMessage.put("meta", "{ " +
                "\"notification\": 190, " +
                "\"version\": \"5e13d3e3-acbd-4256-9196-781369c2d143\", " +
                "\"application\": \"c5d6993f-7e81-4039-8755-5b82694bf473\", " +
                "\"stamp\": { ");
        remoteMessage.put("render", "{ \"title\": \"tutu\", \"body\": \"tutu \" }");
        remoteMessage.put("tracking", "{}");

        return remoteMessage;
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

        remoteMessage.put("render", "{ \"title\": \"tutu\", \"body\": \"tutu\" }");

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

    public com.np6.npush.Config getValidConfig() {
        return new com.np6.npush.Config(UUID.randomUUID(), "MCOM032", "default_channel", false);
    }

    @Test(expected = IllegalArgumentException.class)
    public void initializationWithNullParameters() {

        com.np6.npush.Config config = this.getValidConfig();

        NotificationCenter.initialize(null , config);
    }

    @Test(expected = JsonProcessingException.class)
    public void parseInvalidRemoteMessage() throws JsonProcessingException {
        Map<String, String> remoteMessage = this.getInvalidRemoteMessage();
        NotificationCenter.fromRemoteMessage(remoteMessage);
    }

    @Test
    public void parseValidRemoteMessage() throws JsonProcessingException {
        Map<String, String> remoteMessage = this.getValidRemoteMessage();

        Notification notification = NotificationCenter.fromRemoteMessage(remoteMessage);

        assertEquals(190, notification.getMeta().notification);

        assertEquals("5e13d3e3-acbd-4256-9196-781369c2d143", notification.getMeta().version);

        assertEquals("c5d6993f-7e81-4039-8755-5b82694bf473",notification.getMeta().application);

        assertEquals("plop", notification.getMeta().channel);

        assertEquals("q5bVk53ZsGrIPMW681JYfZ5Fgvo_LtDKZXpZmAmgPxw80imc08vrCSSfoaKiWLQe4dCBwG8r6t6rWhoRjnLkMgLq", notification.getTracking().dismiss);

        assertEquals("mBgK2q_QgU8X_fg_PP2NWzxAt6gU0bLVQmBhZ9xnk6Z-qINfy5oDPaQFHzBxMV5SY_S-vYILgRcIfZj7TQj495sJIglj", notification.getTracking().impression);

        assertEquals("fdSrGlqMubuRK_DkREvnd3oeH6bTzVO8AJyuuoeESSKDIxgsxw83C4fi0t6rsiqC0i7yJg3sNC4UotQSHFmqzde1vYqcu4_CbLwX4cRU9OHcG76FGjSZutV1GoreBkp2hyUIZVJhjA", notification.getTracking().redirection);

        assertEquals("lvCUOzBF3NmCv98FI24iRCUoo2BPbjmgq_iRsFPpOfPOVwYO58YSFsUK9neUVDoGTb_WEdw4kuMX3UpAoFB2Yjc", notification.getTracking().optout.global);

        assertEquals("2vNX29icDGokLLNOBZZwmgZhE0D8jkv4bCzBW34PKuzG9O6G-U4Ingb49RwesFtDMEHSPgC5mNYYpFmG0qeW8eBbde6QlflExVejbpy16QVK", notification.getTracking().optout.channel);
    }

    @Test
    public void buildNotificationFromValidMessage() throws Exception {

        int notificationId = 101;

        Map<String, String> remoteMessage = this.getValidRemoteMessage();

        final Notification notification = NotificationCenter.fromRemoteMessage(remoteMessage);

        android.app.Notification buildNotification = NotificationCenter
                .initialize(context, this.getValidConfig())
                .build(notification);


        assertEquals("tutu", buildNotification.extras.get("android.text"));

    }


    @Test
    @Config(sdk = 30)
    public void testBuildValidNotificationWithExistingChannel() throws Exception {

        Meta metaMock = Mockito.mock(Meta.class);
        Tracking trackingMock = Mockito.mock(Tracking.class);
        Render renderMock = Mockito.mock(Render.class);

        Mockito.when(metaMock.getChannelId()).thenReturn("this_my_channel");

        Notification fakeNotification = new Notification()
                .setMeta(metaMock)
                .setRender(renderMock)
                .setTracking(trackingMock);


        NotificationBuilder notificationCompatBuilderMock = Mockito.mock(NotificationBuilder.class, RETURNS_SELF);
        com.np6.npush.Config configMock = Mockito.mock(com.np6.npush.Config.class, RETURNS_SELF);
        NotificationManager notificationManagerMock = Mockito.mock(NotificationManager.class);
        NotificationManagerCompat notificationManagerCompatMock = Mockito.mock(NotificationManagerCompat.class);

        NotificationCenter notificationCenter = new NotificationCenter(
                configMock,
                notificationCompatBuilderMock,
                notificationManagerMock,
                notificationManagerCompatMock
        );

        notificationCenter.build(fakeNotification);

        Mockito.verify(renderMock, Mockito.times(1)).getTitle();
        Mockito.verify(renderMock, Mockito.times(1)).getBody();

        Mockito.verify(metaMock, Mockito.times(1)).getRedirection();
        Mockito.verify(metaMock, Mockito.times(3)).getChannelId();

        Mockito.verify(configMock, Mockito.times(0)).getDefaultChannel();
    }

    @Test
    @Config(sdk = 30)
    public void testBuildValidNotificationWithNonExistingChannel() throws Exception {

        Meta metaMock = Mockito.mock(Meta.class);
        Tracking trackingMock = Mockito.mock(Tracking.class);
        Render renderMock = Mockito.mock(Render.class);

        Mockito.when(metaMock.getChannelId()).thenReturn(null);

        Notification fakeNotification = new Notification()
                .setMeta(metaMock)
                .setRender(renderMock)
                .setTracking(trackingMock);


        NotificationBuilder notificationCompatBuilderMock = Mockito.mock(NotificationBuilder.class, RETURNS_SELF);
        com.np6.npush.Config configMock = Mockito.mock(com.np6.npush.Config.class, RETURNS_SELF);
        NotificationManager notificationManagerMock = Mockito.mock(NotificationManager.class);
        NotificationManagerCompat notificationManagerCompatMock = Mockito.mock(NotificationManagerCompat.class);

        NotificationCenter notificationCenter = new NotificationCenter(
                configMock,
                notificationCompatBuilderMock,
                notificationManagerMock,
                notificationManagerCompatMock
        );

        notificationCenter.build(fakeNotification);

        Mockito.verify(renderMock, Mockito.times(1)).getTitle();
        Mockito.verify(renderMock, Mockito.times(1)).getBody();

        Mockito.verify(metaMock, Mockito.times(1)).getRedirection();
        Mockito.verify(metaMock, Mockito.times(1)).getChannelId(); // due to check

        Mockito.verify(configMock, Mockito.times(1)).getDefaultChannel();
    }

    @Test
    @Config(sdk = 23)
    public void testBuildValidNotificationWithoutSystemChannels() throws Exception {

        Meta metaMock = Mockito.mock(Meta.class);
        Tracking trackingMock = Mockito.mock(Tracking.class);
        Render renderMock = Mockito.mock(Render.class);

        Mockito.when(metaMock.getChannelId()).thenReturn("this_my_channel");

        Notification fakeNotification = new Notification()
                .setMeta(metaMock)
                .setRender(renderMock)
                .setTracking(trackingMock);


        NotificationBuilder notificationCompatBuilderMock = Mockito.mock(NotificationBuilder.class, RETURNS_SELF);
        com.np6.npush.Config configMock = Mockito.mock(com.np6.npush.Config.class, RETURNS_SELF);
        NotificationManager notificationManagerMock = Mockito.mock(NotificationManager.class);
        NotificationManagerCompat notificationManagerCompatMock = Mockito.mock(NotificationManagerCompat.class);

        NotificationCenter notificationCenter = new NotificationCenter(
                configMock,
                notificationCompatBuilderMock,
                notificationManagerMock,
                notificationManagerCompatMock
        );

        notificationCenter.build(fakeNotification);

        Mockito.verify(metaMock, Mockito.times(0)).getChannelId();
        Mockito.verify(configMock, Mockito.times(0)).getDefaultChannel();

    }

    @Test
    @Config(sdk = 30)
    public void testSubmitNotificationWithDisabledNotifications() throws Exception {

        Meta metaMock = Mockito.mock(Meta.class);
        Tracking trackingMock = Mockito.mock(Tracking.class);
        Render renderMock = Mockito.mock(Render.class);

        Mockito.when(metaMock.getChannelId()).thenReturn(null);

        Notification fakeNotification = new Notification()
                .setMeta(metaMock)
                .setRender(renderMock)
                .setTracking(trackingMock);


        NotificationBuilder notificationCompatBuilderMock = Mockito.mock(NotificationBuilder.class, RETURNS_SELF);
        com.np6.npush.Config configMock = Mockito.mock(com.np6.npush.Config.class, RETURNS_SELF);
        NotificationManager notificationManagerMock = Mockito.mock(NotificationManager.class);
        NotificationManagerCompat notificationManagerCompatMock = Mockito.mock(NotificationManagerCompat.class);


        Mockito.when(notificationManagerCompatMock.areNotificationsEnabled()).thenReturn(false);

        NotificationCenter notificationCenter = new NotificationCenter(
                configMock,
                notificationCompatBuilderMock,
                notificationManagerMock,
                notificationManagerCompatMock
        );

        notificationCenter.submit(fakeNotification).get();

        ArgumentCaptor<Integer> notificationId = ArgumentCaptor.forClass(Integer.class);
        ArgumentCaptor<android.app.Notification> notification = ArgumentCaptor.forClass(android.app.Notification.class);


        Mockito.verify(trackingMock, Mockito.times(1)).getGlobalOptoutAction();
        Mockito.verify(trackingMock, Mockito.times(0)).getChannelOptoutAction();
        Mockito.verify(notificationManagerMock, Mockito.times(0)).notify(notificationId.capture(), notification.capture());

    }
    @Test
    @Config(sdk = 30)
    public void testSubmitNotificationWithActivatedNotificationsAndDisabledChannel() throws Exception {

        Meta metaMock = Mockito.mock(Meta.class);
        Tracking trackingMock = Mockito.mock(Tracking.class);
        Render renderMock = Mockito.mock(Render.class);

        Mockito.when(metaMock.getChannelId()).thenReturn("test_channel");

        Notification fakeNotification = new Notification()
                .setMeta(metaMock)
                .setRender(renderMock)
                .setTracking(trackingMock);

        NotificationBuilder notificationCompatBuilderMock = Mockito.mock(NotificationBuilder.class, RETURNS_SELF);
        com.np6.npush.Config configMock = Mockito.mock(com.np6.npush.Config.class, RETURNS_SELF);
        NotificationManager notificationManagerMock = Mockito.mock(NotificationManager.class);
        NotificationManagerCompat notificationManagerCompatMock = Mockito.mock(NotificationManagerCompat.class);


        Mockito.when(notificationManagerCompatMock.areNotificationsEnabled()).thenReturn(true);
        Mockito.when(notificationManagerMock.getNotificationChannel(ArgumentMatchers.anyString())).thenReturn(null);

        NotificationCenter notificationCenter = new NotificationCenter(
                configMock,
                notificationCompatBuilderMock,
                notificationManagerMock,
                notificationManagerCompatMock
        );

        notificationCenter.submit(fakeNotification).get();

        ArgumentCaptor<Integer> notificationId = ArgumentCaptor.forClass(Integer.class);
        ArgumentCaptor<android.app.Notification> notification = ArgumentCaptor.forClass(android.app.Notification.class);

        Mockito.verify(trackingMock, Mockito.times(0)).getGlobalOptoutAction();
        Mockito.verify(trackingMock, Mockito.times(1)).getChannelOptoutAction();
        Mockito.verify(notificationManagerMock, Mockito.times(0)).notify(notificationId.capture(), notification.capture());
    }

    @Test
    @Config(sdk = 30)
    public void testSubmitNotificationWithActivatedNotifications() throws Exception {

        Meta metaMock = Mockito.mock(Meta.class);
        Tracking trackingMock = Mockito.mock(Tracking.class);
        Render renderMock = Mockito.mock(Render.class);

        Mockito.when(metaMock.getChannelId()).thenReturn("test_channel");

        Notification fakeNotification = new Notification()
                .setMeta(metaMock)
                .setRender(renderMock)
                .setTracking(trackingMock);

        NotificationChannel fakeChannel = new NotificationChannel("id", "test_channel", NotificationManager.IMPORTANCE_DEFAULT);

        NotificationBuilder notificationCompatBuilderMock = Mockito.mock(NotificationBuilder.class, RETURNS_SELF);
        com.np6.npush.Config configMock = Mockito.mock(com.np6.npush.Config.class, RETURNS_SELF);
        NotificationManager notificationManagerMock = Mockito.mock(NotificationManager.class);
        NotificationManagerCompat notificationManagerCompatMock = Mockito.mock(NotificationManagerCompat.class);


        Mockito.when(notificationManagerCompatMock.areNotificationsEnabled()).thenReturn(true);
        Mockito.when(notificationManagerMock.getNotificationChannel(ArgumentMatchers.anyString())).thenReturn(fakeChannel);

        NotificationCenter notificationCenter = new NotificationCenter(
                configMock,
                notificationCompatBuilderMock,
                notificationManagerMock,
                notificationManagerCompatMock
        );

        notificationCenter.submit(fakeNotification).get();

        ArgumentCaptor<Integer> notificationId = ArgumentCaptor.forClass(Integer.class);
        ArgumentCaptor<android.app.Notification> notification = ArgumentCaptor.forClass(android.app.Notification.class);

        Mockito.verify(trackingMock, Mockito.times(0)).getGlobalOptoutAction();
        Mockito.verify(trackingMock, Mockito.times(0)).getChannelOptoutAction();
        Mockito.verify(notificationManagerMock, Mockito.times(1)).notify(notificationId.capture(), notification.capture());

    }

}