package com.np6.npush.internal.core;

import android.util.Log;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.np6.npush.Config;
import com.np6.npush.NPush;
import com.np6.npush.internal.api.TelemetryApi;
import com.np6.npush.internal.models.log.common.Info;
import com.np6.npush.internal.models.log.common.Warning;

public class Logger {

    public static final String TAG = "np6-messaging";

    public static void Info(com.np6.npush.internal.models.log.common.Info<String> log) {
        try {
            Log.i(TAG, log.value);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public static void Error(com.np6.npush.internal.models.log.common.Error<Throwable> log) {
        try {

            Log.e(TAG, log.value.getLocalizedMessage());

            if (NPush.Instance().getConfig().isTelemetryActivated()) {

                Config config = NPush.Instance().getConfig();

                TelemetryApi telemetryApi = TelemetryApi.create(config.getIdentity());

                telemetryApi
                        .log(log)
                        .thenAccept(response -> {
                            if (response.isSuccessful()) {
                                Logger.Info(new Info<>("Successfully sent exception log to telemetry API"));
                                return;
                            }
                            Logger.Warning(new Warning<>(new Exception("Sending telemetry log failed with http status code " + response.code())));

                        }).exceptionally((throwable -> {
                            Logger.Warning(new Warning<>(throwable));
                            return null;
                        }));
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public static void Warning(com.np6.npush.internal.models.log.common.Warning<Throwable> log) {
        try {
            Log.w(TAG, log.value.getLocalizedMessage());
        } catch (Exception e) {
            e.printStackTrace();
        }
    }


}
