package com.np6.npush.internal.service;

import android.content.Context;

import com.np6.npush.internal.repository.TokenRepository;

import java.util.Objects;

public class TokenService {

    private final TokenRepository repository;

    public static TokenService Create(Context context) {
        if (Objects.isNull(context)) {
            throw new IllegalArgumentException();
        }

        return new TokenService(context);
    }

    private TokenService(Context context) {
        this.repository = TokenRepository.create(context);
    }

    public String Get() {
        return this.repository.Get();
    }

}
