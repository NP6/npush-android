package com.np6.npush.internal.core;

import android.util.Log;

import com.fasterxml.jackson.core.JsonProcessingException;

public class Logger {

    public static final String TAG = "np6-messaging";

    private static final Serializer serializer = new Serializer();

    public static void Info(com.np6.npush.internal.models.log.Log log) {
        try {
            String value = serializer.serialize(log);
            Log.i(TAG , value);
        } catch (JsonProcessingException e) {
            e.printStackTrace();
        }
    }

    public static void Error(com.np6.npush.internal.models.log.Log log) {
        try {
            String value = serializer.serialize(log);
            Log.e(TAG , value);
        } catch (JsonProcessingException e) {
            e.printStackTrace();
        }
    }

}
