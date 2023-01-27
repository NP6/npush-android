package com.np6.npush.internal.core;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.DeserializationFeature;
import com.fasterxml.jackson.databind.ObjectMapper;

import java.util.Map;

public class Serializer {

    private final static ObjectMapper objectMapper = new ObjectMapper();

    static {
        objectMapper.configure(DeserializationFeature.FAIL_ON_UNKNOWN_PROPERTIES, false);
    }

    /**
     *
     * @param object object
     * @param <T> input model to serialize
     * @return json serialized object
     * @throws JsonProcessingException
     */
    public <T> String serialize(T object) throws JsonProcessingException {
        return objectMapper.writeValueAsString(object);
    }

    /**
     *
     * @param object json string representation on the object
     * @param target object to deserialize
     * @param <T> output model to deserialize
     * @return A object of type T
     * @throws JsonProcessingException
     */
    public <T> T deserialize(String object, Class<T> target) throws JsonProcessingException {
        return objectMapper.readValue(object, target);
    }

    /**
     *
     * @param object json string representation on the object
     * @param target object to deserialize
     * @param <T> output model to deserialize
     * @return A object of type T
     * @throws JsonProcessingException
     */
    public <T> T deserialize(Map<String, String> object, Class<T> target) throws JsonProcessingException {
        String str =  objectMapper.writeValueAsString(object);
        return objectMapper.readValue(str, target);
    }
}
