package com.np6.npush.internal;

import android.content.Context;
import android.os.Build;

import com.np6.npush.Config;
import com.np6.npush.internal.api.Api;
import com.np6.npush.internal.api.SubscriptionApi;
import com.np6.npush.internal.models.Subscription;
import com.np6.npush.internal.models.common.Completion;
import com.np6.npush.internal.models.common.Result;
import com.np6.npush.internal.models.contact.Linked;
import com.np6.npush.internal.models.gateway.Firebase;
import com.np6.npush.internal.repository.IdentifierRepository;
import com.np6.npush.internal.repository.TokenRepository;

import java.util.Locale;
import java.util.Map;
import java.util.Objects;
import java.util.UUID;

public class Installation {

    private final Config config;

    private final TokenRepository tokenRepository;

    private final IdentifierRepository identifierRepository;

    public static Installation initialize(Context context, Config config) {

        if (Objects.isNull(config)) {
            throw new IllegalArgumentException();
        }

        if (Objects.isNull(context)) {
            throw new IllegalArgumentException();
        }

        return new Installation(context, config);
    }

    private Installation(Context context, Config config) {
        this.config = config;
        this.tokenRepository = TokenRepository.Create(context);
        this.identifierRepository = IdentifierRepository.Create(context);
    }


    public void subscribe(Linked linked, Completion<Subscription> completion) {
        try  {

            String token = this.tokenRepository.Get();

            if (Objects.isNull(token))
                throw new IllegalArgumentException("token is null");

            Subscription subscription = new Subscription()
                    .setId(this.getIdentifer())
                    .setApplication(config.getApplication())
                    .setGateway(new Firebase(token))
                    .setLinked(linked)
                    .setProtocol("1.0.0")
                    .setCulture(Locale.getDefault().getLanguage());

            SubscriptionApi api = new SubscriptionApi(config.getIdentity());

            api.put(subscription, completion);

        } catch (Exception exception) {
            completion.onComplete(new Result.Error<>(exception));
        }
    }


    private UUID getIdentifer() {
        return this.identifierRepository.Exist()
                ? this.identifierRepository.Get()
                : this.identifierRepository.Add(UUID.randomUUID());
    }

}

