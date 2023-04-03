package com.np6.npush.internal;

import android.content.Context;

import com.np6.npush.Config;
import com.np6.npush.internal.api.SubscriptionApi;
import com.np6.npush.internal.models.Subscription;
import com.np6.npush.internal.models.contact.Linked;
import com.np6.npush.internal.models.gateway.Firebase;
import com.np6.npush.internal.models.gateway.Gateway;
import com.np6.npush.internal.models.log.common.Error;
import com.np6.npush.internal.models.log.common.Info;
import com.np6.npush.internal.repository.IdentifierRepository;
import com.np6.npush.internal.repository.TokenRepository;

import java.util.Locale;
import java.util.Objects;
import java.util.UUID;

import java9.util.concurrent.CompletableFuture;
import okhttp3.Response;

public class Installation {

    private final Config config;

    private final TokenRepository tokenRepository;

    private final IdentifierRepository identifierRepository;

    private final SubscriptionApi subscriptionApi;

    public static Installation create(Context context, Config config) {

        if (config == null) {
            throw new IllegalArgumentException();
        }

        if (context == null) {
            throw new IllegalArgumentException();
        }

        TokenRepository tokenRepository = TokenRepository.create(context);

        IdentifierRepository identifierRepository = IdentifierRepository.create(context);

        SubscriptionApi subscriptionApi =  SubscriptionApi.create(config.getIdentity());

        return new Installation(config, tokenRepository, identifierRepository, subscriptionApi);
    }

    public Installation(
            Config config,
            TokenRepository tokenRepository,
            IdentifierRepository identifierRepository,
            SubscriptionApi subscriptionApi)
    {
        this.config = config;
        this.tokenRepository = tokenRepository;
        this.identifierRepository = identifierRepository;
        this.subscriptionApi = subscriptionApi;
    }


    public CompletableFuture<Response> subscribe(Linked linked) {

        try {
            if (linked == null)
                throw new IllegalArgumentException("linked cannot be null");

            String token = this.tokenRepository.Get();

            if (token == null || token.isEmpty())
                throw new IllegalArgumentException("token cannot be null or empty");

            Firebase gateway = new Firebase(token);

            Subscription subscription = new Subscription()
                    .setId(this.getIdentifier())
                    .setApplication(config.getApplication())
                    .setGateway(gateway)
                    .setLinked(linked)
                    .setProtocol("1.0.0")
                    .setCulture(Locale.getDefault().getLanguage());

            return this.subscriptionApi.put(subscription);

        } catch (Exception exception) {
            return CompletableFuture.failedFuture(exception);
        }
    }


    public UUID getIdentifier() {
        return this.identifierRepository.Exist()
                ? this.identifierRepository.Get()
                : this.identifierRepository.Add(UUID.randomUUID());
    }

}

