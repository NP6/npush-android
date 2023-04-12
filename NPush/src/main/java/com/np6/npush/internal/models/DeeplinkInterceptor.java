package com.np6.npush.internal.models;

import android.app.TaskStackBuilder;
import android.content.Context;

public interface DeeplinkInterceptor {

    public TaskStackBuilder getTaskStackBuilder(Context context, String deeplink);

}
