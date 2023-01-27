package com.np6.npush.internal.models.notification.input;

import com.fasterxml.jackson.annotation.JsonCreator;
import com.fasterxml.jackson.annotation.JsonProperty;


public class Data {

    public Meta meta;
    public Render render;
    public Tracking tracking;

    @JsonCreator(mode = JsonCreator.Mode.PROPERTIES)
    public Data(
            @JsonProperty("meta") Meta meta,
            @JsonProperty("render") Render render,
            @JsonProperty("tracking") Tracking tracking)
    {
        this.meta = meta;
        this.render = render;
        this.tracking = tracking;
    }
}
