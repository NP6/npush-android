package com.np6.npush.internal.core.persistence;

public interface Storage {
    /**
     * Fetch the given key
     * @param key
     * @return
     */
    String fetch(String key);

    /**
     * Add or update a specific key
     * @param key
     * @param value
     */
    void put(String key, String value);

    /**
     * Remove a specific key
     * @param key
     */
    void remove(String key);

    /**
     * Check if a key exist
     * @param key
     * @return
     */
    boolean exist(String key);

}
