package com.np6.npush.internal.repository;

import android.content.Context;

import com.np6.npush.internal.core.Constants;
import com.np6.npush.internal.core.persistence.SharedPreferenceStorage;
import com.np6.npush.internal.core.persistence.Storage;

import java.util.Objects;
import java.util.UUID;

public class IdentifierRepository implements Repository<UUID> {

    private static final String IDENTIFIER_KEY = "identifier";

    private final Storage storage;

    public static IdentifierRepository create(Context context) {
        return new IdentifierRepository(context);
    }

    private IdentifierRepository(Context context) {
        this.storage = new SharedPreferenceStorage(Constants.Repository.IDENTIFIER_REPOSITORY_NAMESPACE, context);
    }

    @Override
    public UUID Get() {
            String value = this.storage.fetch(IDENTIFIER_KEY);

            /*
            *TODO :
            * Throw IllegalArgumentException if value is null ?
            * Case happen if we call Remove() and then Get()
            */
            if (Objects.isNull(value)) {
                return null;
            }

            return UUID.fromString(value);
    }

    @Override
    public UUID Add(UUID identifier) {
            this.storage.put(IDENTIFIER_KEY, identifier.toString());

            return identifier;
    }

    @Override
    public Boolean Exist() {
        return this.storage.exist(IDENTIFIER_KEY);
    }

    @Override
    public void Remove() {
        this.storage.remove(IDENTIFIER_KEY);
    }
}
