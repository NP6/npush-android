package com.np6.npush.internal.models.notification.input;

import com.fasterxml.jackson.annotation.JsonCreator;
import com.fasterxml.jackson.annotation.JsonProperty;

import java.util.UUID;

public class Stamp {

    public long time;

    public UUID id;

    public UUID thread;

    public UUID set;

    @JsonCreator
    public Stamp(
            @JsonProperty("time") long time,
            @JsonProperty("id") UUID id,
            @JsonProperty("thread") UUID thread,
            @JsonProperty("set") UUID set)
    {
        this.time = time;
        this.id = id;
        this.thread = thread;
        this.set = set;
    }
}
