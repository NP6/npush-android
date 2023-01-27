package com.np6.npush.internal.core.network.interceptor;

import android.os.Build;

import androidx.annotation.NonNull;

import java.io.IOException;
import java.util.Locale;

import okhttp3.Interceptor;
import okhttp3.Request;
import okhttp3.Response;

public class UserAgentInterceptor implements Interceptor {
    private final String mUserAgent;

    public UserAgentInterceptor() {
        mUserAgent = (String.format(Locale.US,
                "Android : %s; Model : %s; Brand : %s; Device : %s; ID : %s; Product : %s; VERSION_SDK : %s; VERSION_RELEASE : %s; VERSION_INCREMENTAL : %s; Language : %s",
                Build.VERSION.RELEASE,
                Build.MODEL,
                Build.BRAND,
                Build.DEVICE,
                Build.ID,
                Build.PRODUCT,
                Build.VERSION.SDK,
                Build.VERSION.RELEASE,
                Build.VERSION.INCREMENTAL,
                Locale.getDefault().getLanguage()));
    }

    @Override
    public Response intercept(@NonNull Chain chain) throws IOException {
        Request request = chain.request()
                .newBuilder()
                .header("User-Agent", mUserAgent)
                .build();
        return chain.proceed(request);
    }
}