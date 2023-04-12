package com.np6.demo.services;

import androidx.annotation.NonNull;

import com.google.firebase.messaging.FirebaseMessagingService;
import com.google.firebase.messaging.RemoteMessage;
import com.np6.npush.Config;
import com.np6.npush.NPush;

import java.util.UUID;

public class MyFirebaseMessagingService extends FirebaseMessagingService {

    @Override
    public void onMessageReceived(RemoteMessage remoteMessage) {

        Config config = new Config(
                UUID.fromString("c5d6993f-7e81-4039-8755-5b82694bf473"),
                "MCOM032",
                "marketingChannel",
                true
        );

        NPush.Instance().setConfig(config);

        NPush.Instance().handleNotification(this.getApplicationContext(), remoteMessage.getData());
    }

    @Override
    public void onNewToken(@NonNull String token) {
        super.onNewToken(token);
    }
}
