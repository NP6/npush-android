package com.np6.npush.internal.models.gateway;

import com.fasterxml.jackson.annotation.JsonCreator;
import com.fasterxml.jackson.annotation.JsonProperty;

public class Firebase extends Gateway {

    public String token;

    @JsonCreator(mode = JsonCreator.Mode.PROPERTIES)
    public Firebase(@JsonProperty("token") String token) {
        this.token = token;
    }

}
