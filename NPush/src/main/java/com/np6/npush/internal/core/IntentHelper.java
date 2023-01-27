package com.np6.npush.internal.core;

import android.app.PendingIntent;
import android.content.Context;
import android.content.Intent;
import android.net.Uri;

import java.util.Map;
import java.util.Random;

public class IntentHelper {


    public static PendingIntent CreatePendingActivityIntent(Context context, Intent intent) {
        return PendingIntent.getActivity(
                context,
                new Random().nextInt(),
                intent,
                PendingIntent.FLAG_UPDATE_CURRENT | PendingIntent.FLAG_IMMUTABLE
        );
    }
    public static PendingIntent CreatePendingBroadcastIntent(Context context, Intent intent) {
        return PendingIntent.getBroadcast(
                context,
                new Random().nextInt(),
                intent,
                PendingIntent.FLAG_UPDATE_CURRENT | PendingIntent.FLAG_IMMUTABLE
        );
    }


    public static Intent CreateIntent(String action, Uri data, Map<String, String> extras) throws Exception {
        Intent intent = new Intent();

        if (action == null) {
            throw new Exception("Intent cannot be created without action");
        }
        intent.setAction(action);

        if (data != null) {
            intent.setData(data);
        }
        for (Map.Entry<String, String> extra : extras.entrySet()) {
            intent.putExtra(extra.getKey(), extra.getValue());
        }

        return intent;
    }

    public static <T> Intent CreateIntent(Context context, String action, Class<T> cls, Map<String, String> extras) throws Exception {
        Intent intent = new Intent(context, cls);

        if (action == null) {
            throw new Exception("Intent cannot be created without action");
        }

        intent.setAction(action);

        for (Map.Entry<String, String> extra : extras.entrySet()) {
            intent.putExtra(extra.getKey(), extra.getValue());
        }

        return intent;
    }

    public static <T> Intent CreateIntent(Context context, Class<T> cls, Map<String, String> extras)  {
        Intent intent = new Intent(context, cls);

        intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);

        for (Map.Entry<String, String> extra : extras.entrySet()) {
            intent.putExtra(extra.getKey(), extra.getValue());
        }

        return intent;
    }

}
