package com.np6.npush.internal.system;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;

import com.np6.npush.internal.Interaction;
import com.np6.npush.internal.core.Constants;
import com.np6.npush.internal.core.Logger;
import com.np6.npush.internal.models.action.TrackingAction;
import com.np6.npush.internal.models.common.Result;
import com.np6.npush.internal.models.log.common.Error;
import com.np6.npush.internal.models.log.common.Info;

public class ActionBroadcastReceiver extends BroadcastReceiver {

    @Override
    public void onReceive(Context context, Intent intent) {
        final PendingResult pendingResult = goAsync();

        try {
            switch (intent.getAction()) {
                case Constants.Intent.INTENT_DISMISS_INTENT:
                    CreateDismissAction(intent);
                    return;
                case Constants.Intent.INTENT_REDIRECTION_INTENT:
                    CreateRedicretionAction(intent);
            }
        } catch (Exception exception) {
            Logger.Error(new Error<>(exception));
        } finally {
            pendingResult.finish();
        }
    }

    private void CreateRedicretionAction(Intent intent) {
        final String redirection = intent.getStringExtra(Constants.Extra.BUNDLE_KEY_REDIRECTION_KEY);
        final String radical = intent.getStringExtra(Constants.Extra.BUNDLE_RADICAL_KEY);

        TrackingAction<String> action = new TrackingAction<>(redirection, radical);

        Interaction.handle(action, (result) -> {
            if (result instanceof Result.Error)
                Logger.Error(new Error<>(((Result.Error) result).exception));

            if (result instanceof Result.Success)
                Logger.Info(new Info<>("action tracked successfully"));

        });
    }


    private void CreateDismissAction(Intent intent) {
        final String dismiss = intent.getStringExtra(Constants.Extra.BUNDLE_KEY_DISMISS_KEY);
        final String radical = intent.getStringExtra(Constants.Extra.BUNDLE_RADICAL_KEY);

        TrackingAction<String> action = new TrackingAction<>(dismiss, radical);
        Interaction.handle(action, (result) -> {
            if (result instanceof Result.Error)
                Logger.Error(new Error<>(((Result.Error) result).exception));

            if (result instanceof Result.Success)
                Logger.Info(new Info<>("action tracked successfully"));

        });

    }
}
