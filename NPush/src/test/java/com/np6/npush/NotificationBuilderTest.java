package com.np6.npush;

import android.app.NotificationManager;
import android.content.Context;

import androidx.test.core.app.ApplicationProvider;

import org.junit.Before;
import org.junit.runner.RunWith;
import org.robolectric.RobolectricTestRunner;
import org.robolectric.annotation.Config;

import java.util.UUID;

@Config(sdk = {30})
@RunWith(RobolectricTestRunner.class)
public class NotificationBuilderTest
{


    Context context = ApplicationProvider.getApplicationContext();
    NotificationManager notificationManager;

    public com.np6.npush.Config getValidConfig() {
        return new com.np6.npush.Config(UUID.randomUUID(), "MCOM032", "default_channel");
    }

    @Before
    public void setUp() {
        NPush.Instance().setConfig(getValidConfig());
        notificationManager = (NotificationManager)context.getSystemService(Context.NOTIFICATION_SERVICE);
    }
}
