package com.np6.npush.internal.core;

import com.np6.npush.BuildConfig;

public class Constants {

    public static final String DEFAULT_MARKETING_CHANNEL = "Default_Marketing_Channel";

    public static class Intent {
        public final static String INTENT_REDIRECTION_INTENT = "com.npush.action.notification.redirect";
        public final static String INTENT_DISMISS_INTENT = "com.npush.action.notification.cancel";
    }

    public static class Extra {
        public final static String BUNDLE_KEY_REDIRECTION_KEY = "com.np6.bundle.extra.tracking.redirection";
        public final static String BUNDLE_DEEPLINK_KEY = "com.np6.bundle.extra.action.deeplink";
        public final static String BUNDLE_RADICAL_KEY = "com.np6.bundle.extra.tracking.radical";
        public final static String BUNDLE_KEY_DISMISS_KEY = "com.np6.bundle.extra.tracking.dismiss";
        public final static String FROM_NPUSH = "com.npush.extra.from.service";

    }

    public static class WebServices {

        public static final String Subscription_Endpoint = BuildConfig.NP6_SUBSCRIPION_API_URL;
        public static final String Telemetry_Endpoint = BuildConfig.NP6_TELEMETRY_API_URL;
    }

    public static class Repository {
        public static final String SUBSCRIPTION_REPOSITORY_NAMESPACE = "com.np6.subscription.repo";
        public static final String TOKEN_REPOSITORY_NAMESPACE = "com.np6.token.repo";
        public static final String IDENTIFIER_REPOSITORY_NAMESPACE = "com.np6.identifier.repo";
    }
}
