package com.np6.npush.internal.system;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;

import com.np6.npush.internal.api.InteractionApi;
import com.np6.npush.internal.core.Constants;
import com.np6.npush.internal.core.Logger;
import com.np6.npush.internal.models.action.TrackingAction;
import com.np6.npush.internal.models.common.Result;
import com.np6.npush.internal.models.log.common.Error;
import com.np6.npush.internal.models.log.common.Info;

import java.util.Objects;

public class ActionBroadcastReceiver extends BroadcastReceiver {

    @Override
    public void onReceive(Context context, Intent intent) {
        final PendingResult pendingResult = goAsync();

        try {
            TrackingAction<String> action = parse(intent);

            InteractionApi interactionApi = InteractionApi.create();

            interactionApi
                    .get(action.getRadical(), action.getValue())
                    .thenAccept(response -> {
                        if (response.isSuccessful()) {
                            Logger.Info(new Info<>("Action tracked successfully "));
                            return;
                        }
                        Logger.Error(new Error<>(new Exception("Action tracking failed - status code : " + response.code())));

                    }).exceptionally((throwable -> {
                        Logger.Error(new Error<>(throwable));
                        return null;
                    }));

        } catch (Exception exception) {
            Logger.Error(new Error<>(exception));
        } finally {
            pendingResult.finish();
        }
    }

    public static String parseAction(Intent intent) throws Exception {
        switch (intent.getAction()) {
            case Constants.Intent.INTENT_DISMISS_INTENT:
                return intent.getStringExtra(Constants.Extra.BUNDLE_KEY_DISMISS_KEY);
            case Constants.Intent.INTENT_REDIRECTION_INTENT:
                return intent.getStringExtra(Constants.Extra.BUNDLE_KEY_REDIRECTION_KEY);
            default:
                throw new Exception("unknown intent action key");
        }
    }

    public static TrackingAction<String> parse(Intent intent) throws Exception {

        final String radical = intent.getStringExtra(Constants.Extra.BUNDLE_RADICAL_KEY);

        if (radical == null || radical.isEmpty()) {
            throw new IllegalArgumentException("radical cannot be null or empty");
        }

        final String action = parseAction(intent);

        if (action == null || action.isEmpty()) {
            throw new IllegalArgumentException("radical cannot be null or empty");
        }

        return new TrackingAction<>(action, radical);

    }
}
