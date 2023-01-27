package com.np6.npush.internal.models.notification;

import com.fasterxml.jackson.annotation.JsonCreator;
import com.fasterxml.jackson.annotation.JsonProperty;
import com.np6.npush.internal.models.notification.input.Meta;
import com.np6.npush.internal.models.notification.input.Render;
import com.np6.npush.internal.models.notification.input.Tracking;

public class Notification {

    @JsonProperty("render")
    public Render render;

    @JsonProperty("tracking")
    public Tracking tracking;

    @JsonProperty("meta")
    public Meta meta;

    @JsonCreator(mode = JsonCreator.Mode.PROPERTIES)
    public Notification() {}

    public Notification setMeta(Meta meta) {
        this.meta = meta;
        return this;
    }

    public Notification setRender(Render render) {
        this.render = render;
        return this;
    }

    public Notification setTracking(Tracking tracking) {
        this.tracking = tracking;
        return this;
    }

    public Tracking getTracking() {
        return tracking;
    }

    public Meta getMeta() {
        return meta;
    }

    public Render getRender() {
        return render;
    }
}