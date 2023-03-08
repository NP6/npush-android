package com.np6.npush.internal.service;

import android.content.Context;

import com.np6.npush.internal.repository.IdentifierRepository;

import java.util.Objects;
import java.util.UUID;

public class IdentifierService {

    private final IdentifierRepository repository;

    public static IdentifierService Create(Context context) {
        if (Objects.isNull(context)) {
            throw new IllegalArgumentException();
        }

        return new IdentifierService(context);
    }

    private IdentifierService(Context context) {
        this.repository = IdentifierRepository.create(context);
    }

    public UUID FetchRepository() {
        return this.repository.Get();
    }


    public Boolean CheckIfExist() {
        return this.repository.Exist();
    }

    public UUID Add(UUID identifier) {
        return this.repository.Add(identifier);
    }

}
