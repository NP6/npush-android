package com.np6.npush.internal.system;

import android.app.Activity;
import android.app.PendingIntent;
import android.app.TaskStackBuilder;
import android.content.Intent;
import android.net.Uri;
import android.os.Build;

import com.np6.npush.NPush;
import com.np6.npush.internal.core.Constants;
import com.np6.npush.internal.core.IntentHelper;
import com.np6.npush.internal.core.Logger;
import com.np6.npush.internal.models.DeeplinkInterceptor;
import com.np6.npush.internal.models.log.common.Error;

import java.util.HashMap;
import java.util.Map;

public class TransparentActivity extends Activity {

    @Override
    protected void onStart() {
        try {
            super.onStart();

            launchDeeplink();

            launchTrackingIntent();

            finish();

        } catch (Exception exception) {
            Logger.Error(new Error<>(exception));
            finish();
        }
    }

    public void launchTrackingIntent() throws Exception {
        final Intent intent = getIntent();

        final String redirection = intent.getStringExtra(Constants.Extra.BUNDLE_KEY_REDIRECTION_KEY);
        final String radical = intent.getStringExtra(Constants.Extra.BUNDLE_RADICAL_KEY);

        Map<String, String> data = new HashMap<>();

        data.put(Constants.Extra.BUNDLE_KEY_REDIRECTION_KEY, redirection);
        data.put(Constants.Extra.BUNDLE_RADICAL_KEY, radical);

        Intent redirectionIntent = IntentHelper.CreateIntent(
                this,
                Constants.Intent.INTENT_REDIRECTION_INTENT,
                ActionBroadcastReceiver.class,
                data);

        PendingIntent deeplinkPendingIntent = IntentHelper.CreatePendingBroadcastIntent(this, redirectionIntent);

        deeplinkPendingIntent.send();

    }

    public void launchDeeplink() {
        final Intent intent = getIntent();

        if (intent.getStringExtra(Constants.Extra.BUNDLE_DEEPLINK_KEY) == null) {
            finish();
            return;
        }

        final String deeplink = intent.getStringExtra(Constants.Extra.BUNDLE_DEEPLINK_KEY);

        if (NPush.Instance().interceptor == null) {
            Uri uri = Uri.parse(deeplink);
            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.JELLY_BEAN) {
                uri = uri.normalizeScheme();
            }

            Intent deeplinkIntent = new Intent(Intent.ACTION_VIEW)
                    .addFlags(Intent.FLAG_ACTIVITY_NEW_TASK)
                    .setData(uri);

            startActivity(deeplinkIntent);
            return;
        }

        final DeeplinkInterceptor interceptor = NPush.Instance().interceptor;

        TaskStackBuilder stackBuilder = interceptor.getTaskStackBuilder(this, deeplink);
        stackBuilder.startActivities();
    }
}
