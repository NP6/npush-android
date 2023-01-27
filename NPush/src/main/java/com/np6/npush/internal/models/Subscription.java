package com.np6.npush.internal.models;

import com.fasterxml.jackson.annotation.JsonCreator;
import com.fasterxml.jackson.annotation.JsonProperty;
import com.np6.npush.internal.models.contact.Linked;
import com.np6.npush.internal.models.gateway.Gateway;

import java.util.UUID;

public class Subscription {
    @JsonProperty("id")
    public UUID id;

    @JsonProperty("application")
    public UUID application;

    @JsonProperty("gateway")
    public Gateway gateway;

    @JsonProperty("linked")
    public Linked linked;

    @JsonProperty("protocol")
    public String protocol;

    @JsonProperty("culture")
    public String culture;

    @JsonCreator(mode = JsonCreator.Mode.PROPERTIES)
    public Subscription() {}

    @Override
    public String toString() {
        return "Subscription{" +
                "Id=" + id +
                ", Application=" + application +
                ", Gateway=" + gateway +
                ", Linked=" + linked +
                ", Protocol='" + protocol + '\'' +
                ", Culture='" + culture + '\'' +
                '}';
    }

    public Subscription setId(UUID id) {
        this.id = id;
        return this;
    }

    public Subscription setApplication(UUID application) {
        this.application = application;
        return this;
    }

    public Subscription setCulture(String culture) {
        this.culture = culture;
        return this;
    }

    public Subscription setProtocol(String protocol) {
        this.protocol = protocol;
        return this;
    }

    public Subscription setLinked(Linked linked) {
        this.linked = linked;
        return this;
    }

    public Subscription setGateway(Gateway gateway) {
        this.gateway = gateway;
        return this;
    }
}