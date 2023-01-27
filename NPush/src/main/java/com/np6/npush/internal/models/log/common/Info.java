package com.np6.npush.internal.models.log.common;


import com.fasterxml.jackson.annotation.JsonCreator;
import com.fasterxml.jackson.annotation.JsonProperty;
import com.np6.npush.internal.models.log.Log;

public class Info<T> extends Log {

    public T value;

    @JsonCreator(mode = JsonCreator.Mode.PROPERTIES)
    public Info(@JsonProperty("value") T value) {
        this.value = value;
    }

}
