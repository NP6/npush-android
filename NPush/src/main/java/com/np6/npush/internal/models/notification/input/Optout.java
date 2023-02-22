package com.np6.npush.internal.models.notification.input;

import com.fasterxml.jackson.annotation.JsonProperty;

public class Optout {

    public String global;

    public String channel;

    public Optout(
            @JsonProperty("global") String global,
            @JsonProperty("channel") String channel)
    {
        this.channel = channel;
        this.global = global;
    }


    public String getChannel() {
        return channel;
    }

    public String getGlobal() {
        return global;
    }
}
