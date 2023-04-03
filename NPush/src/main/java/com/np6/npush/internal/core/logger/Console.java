package com.np6.npush.internal.core.logger;

import android.util.Log;

import com.np6.npush.Config;
import com.np6.npush.NPush;
import com.np6.npush.internal.api.TelemetryApi;
import com.np6.npush.internal.core.logger.Logger;
import com.np6.npush.internal.models.log.common.Error;
import com.np6.npush.internal.models.log.common.Info;
import com.np6.npush.internal.models.log.common.Warning;

public class Console implements Logger {

    public static final String TAG = "np6-messaging";

    public Console() {}

    @Override
    public <T> void info(Info<T> log) {
        try {
            Log.i(TAG, (String) log.value);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    @Override
    public <T> void error(Error<T> log) {
        try {
            Throwable throwable = (Throwable) log.value;
            Log.e(TAG, throwable.getLocalizedMessage());
        } catch(Exception e) {
            e.printStackTrace();
        }
    }

    @Override
    public <T> void warning(Warning<T> log) {
        try {
            Log.w(TAG, (String) log.value);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}
