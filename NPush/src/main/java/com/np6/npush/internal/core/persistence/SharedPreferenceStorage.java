package com.np6.npush.internal.core.persistence;

import android.content.Context;
import android.content.SharedPreferences;

public class SharedPreferenceStorage implements Storage {

    private final SharedPreferences preferences;

    public static final String DEFAULT = "DEFAULT_VALUE";

    public SharedPreferenceStorage(String namespace, Context context) {
        this.preferences = context.getSharedPreferences(namespace, Context.MODE_PRIVATE);
    }

    @Override
    public String fetch(String key) {
        String result = preferences.getString(key, DEFAULT);
        if (result.equals(DEFAULT)) {
            return null;
        }
        return result;
    }

    @Override
    public void put(String key, String value) {
        preferences.edit().putString(key, value).apply();
    }

    @Override
    public void remove(String key) {
        preferences.edit().remove(key).apply();
    }

    @Override
    public boolean exist(String key) {
        return preferences.contains(key);
    }
}
