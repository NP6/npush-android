package com.np6.npush;

import static org.mockito.Mockito.mock;

import android.content.Context;

import androidx.test.core.app.ApplicationProvider;

import org.junit.Test;
import org.junit.runner.RunWith;
import org.robolectric.RobolectricTestRunner;
import org.robolectric.annotation.Config;

import java.util.HashMap;
import java.util.Map;

@Config(sdk = {30})
@RunWith(RobolectricTestRunner.class)
public class NotificationCenterTest {

    Context context = ApplicationProvider.getApplicationContext();


//    public Map<String, String> getValidRemoteMessage() {
//
//        Map<String, String> remoteMessage = new HashMap<>();
//
//        remoteMessage.put("meta", "");
//
//    }

    @Test
    public void initializationWithNullParameters() {


    }

    public void parseValidRemoteMessage() {


    }
}