package com.np6.npush.internal.models.log.common;

import com.fasterxml.jackson.annotation.JsonCreator;
import com.fasterxml.jackson.annotation.JsonProperty;
import com.np6.npush.NPush;
import com.np6.npush.internal.models.Identity;
import com.np6.npush.internal.models.log.Log;

public class Error <T> extends Log {

    public T value;

    @JsonCreator(mode = JsonCreator.Mode.PROPERTIES)
    public Error(@JsonProperty("value") T value) {
        this.value = value;
    }


    public void setValue(T value) {
        this.value = value;
    }

}
