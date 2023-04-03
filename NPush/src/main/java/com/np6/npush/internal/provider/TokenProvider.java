package com.np6.npush.internal.provider;

import android.content.Context;

import androidx.annotation.NonNull;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.android.gms.tasks.Tasks;
import com.google.firebase.messaging.FirebaseMessaging;
import com.np6.npush.internal.models.common.Completion;
import com.np6.npush.internal.models.common.Result;

import java9.util.concurrent.CompletableFuture;
import java.util.concurrent.TimeUnit;

public class TokenProvider implements Provider<String> {

    private TokenProvider(Context context) {}

    public static TokenProvider getProvider(Context context) {
        return new TokenProvider(context);
    }

    @Override
    public CompletableFuture<String> getResultAsync() {

        CompletableFuture<String> future = new CompletableFuture<>();

        FirebaseMessaging
                .getInstance()
                .getToken()
                .addOnSuccessListener(future::complete)
                .addOnFailureListener(future::completeExceptionally)
                .addOnCanceledListener(() -> future.cancel(true));

        return future;
    }
}
