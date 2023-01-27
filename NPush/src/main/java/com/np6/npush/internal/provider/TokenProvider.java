package com.np6.npush.internal.provider;

import android.content.Context;

import com.google.android.gms.tasks.Task;
import com.google.android.gms.tasks.Tasks;
import com.google.firebase.messaging.FirebaseMessaging;
import com.np6.npush.internal.core.concurrency.Concurrent;
import com.np6.npush.internal.models.common.Completion;
import com.np6.npush.internal.models.common.Result;

import java.util.concurrent.TimeUnit;

public class TokenProvider implements Provider<String> {

    private TokenProvider(Context context) {}

    public static TokenProvider getProvider(Context context) {
        return new TokenProvider(context);
    }

    @Override
    public void getResultAsync(Completion<String> completion) {
        Concurrent.Shared.executor.submit(() -> {
            try {
                FirebaseMessaging firebaseMessaging = FirebaseMessaging.getInstance();

                Task<String> tokenTask = firebaseMessaging.getToken();

                Tasks.await(tokenTask, 30000L, TimeUnit.MILLISECONDS);

                if (!tokenTask.isSuccessful()) {
                    throw new Exception("Token task failed. Reason : " + tokenTask.getException().getMessage());
                }

                String token = tokenTask.getResult();

                completion.onComplete(new Result.Success<>(token));
            } catch (Exception exception) {
                completion.onComplete(new Result.Error<>(exception));
            }
        });
    }
}
