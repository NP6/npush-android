package com.np6.npush.internal.repository;

import android.content.Context;

import com.np6.npush.internal.core.Constants;
import com.np6.npush.internal.core.persistence.SharedPreferenceStorage;
import com.np6.npush.internal.core.persistence.Storage;

public class TokenRepository implements Repository<String> {


    public static final String TOKEN_KEY = "token";

    private final Storage storage;

    public static TokenRepository create(Context context) {
        SharedPreferenceStorage sharedPreferenceStorage = new SharedPreferenceStorage(Constants.Repository.TOKEN_REPOSITORY_NAMESPACE, context);
        return new TokenRepository(sharedPreferenceStorage);
    }

    public TokenRepository(SharedPreferenceStorage sharedPreferenceStorage) {
        this.storage = sharedPreferenceStorage;
    }

    @Override
    public String Get() {
        return this.storage.fetch(TOKEN_KEY);
    }

    @Override
    public String Add(String token) {
        this.storage.put(TOKEN_KEY, token);
        return token;
    }

    @Override
    public Boolean Exist() {
        return this.storage.exist(TOKEN_KEY);
    }

    @Override
    public void Remove() {
        this.storage.remove(TOKEN_KEY);
    }
}
