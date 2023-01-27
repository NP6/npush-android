package com.np6.npush.internal.models.log;

import com.fasterxml.jackson.annotation.JsonSubTypes;
import com.fasterxml.jackson.annotation.JsonTypeInfo;
import com.np6.npush.internal.models.log.common.Info;

import java.sql.Timestamp;
import java.util.UUID;

@JsonTypeInfo(
        use = JsonTypeInfo.Id.NAME,
        include = JsonTypeInfo.As.PROPERTY,
        property = "type")
@JsonSubTypes({
        @JsonSubTypes.Type(value = Info.class, name = "info"),
        @JsonSubTypes.Type(value = Error.class, name = "error"),
})
public abstract class Log {

    public UUID Id = UUID.randomUUID();

    public Timestamp Timestamp = new Timestamp(System.currentTimeMillis());

}