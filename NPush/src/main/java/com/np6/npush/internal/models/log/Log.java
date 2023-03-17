package com.np6.npush.internal.models.log;

import com.fasterxml.jackson.annotation.JsonProperty;
import com.fasterxml.jackson.annotation.JsonSubTypes;
import com.fasterxml.jackson.annotation.JsonTypeInfo;
import com.np6.npush.internal.models.log.android.Android;
import com.np6.npush.internal.models.log.android.Device;
import com.np6.npush.internal.models.log.common.Info;

import java.sql.Timestamp;
import java.util.UUID;

@JsonTypeInfo(
        use = JsonTypeInfo.Id.NAME,
        property = "type")
@JsonSubTypes({
        @JsonSubTypes.Type(value = Info.class, name = "info"),
        @JsonSubTypes.Type(value = com.np6.npush.internal.models.log.common.Error.class, name = "error"),
        @JsonSubTypes.Type(value = com.np6.npush.internal.models.log.common.Warning.class, name = "warning"),
})
public abstract class Log {

    @JsonProperty("id")
    public UUID id = UUID.randomUUID();

    @JsonProperty("timestamp")
    public Timestamp timestamp = new Timestamp(System.currentTimeMillis());

    @JsonProperty("device")
    public Device device = new Android();

}