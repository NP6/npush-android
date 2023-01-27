package com.np6.npush.internal.repository;

public interface Repository<T> {
    /**
     * Retrieve an element
     * @return instance of stored element
     * @throws Exception
     */
    T Get() throws Exception;

    /**
     * Add an element
     * @param element Type to add in repository
     * @return the new
     */
    T Add(T element) throws Exception;

    /**
     * Check if element exist
     */
    Boolean Exist();

    /**
     *
     * Remove element
     */
    void Remove();

}
