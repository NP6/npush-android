package com.np6.npush.internal.models.notification.input;

import com.fasterxml.jackson.annotation.JsonCreator;
import com.fasterxml.jackson.annotation.JsonProperty;

public class Meta {

    public int notification;
    public String version;
    public String application;
    public String subscription;
    public Stamp stamp;
    public String channel;
    public String redirection;

    @JsonCreator(mode = JsonCreator.Mode.PROPERTIES)
    public Meta(
            @JsonProperty("notification") int notification,
            @JsonProperty("version") String version,
            @JsonProperty("application") String application,
            @JsonProperty("subscription") String subscription,
            @JsonProperty("stamp") Stamp stamp,
            @JsonProperty("channel") String channel,
            @JsonProperty("redirection") String redirection)
    {
        this.notification = notification;
        this.version = version;
        this.application = application;
        this.subscription = subscription;
        this.stamp = stamp;
        this.channel = channel;
        this.redirection = redirection;
    }


    public String getRedirection() {
        return redirection;
    }

    public String getChannelId() {
        return channel;
    }

    public Meta setChannelId(String channel) {
        this.channel = channel;
        return this;
    }
}
