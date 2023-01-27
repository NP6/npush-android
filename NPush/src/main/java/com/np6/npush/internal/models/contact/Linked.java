package com.np6.npush.internal.models.contact;

import com.fasterxml.jackson.annotation.JsonSubTypes;
import com.fasterxml.jackson.annotation.JsonTypeInfo;

@JsonTypeInfo(
        use = JsonTypeInfo.Id.NAME,
        include = JsonTypeInfo.As.PROPERTY,
        property = "type")
@JsonSubTypes({
        @JsonSubTypes.Type(value = ContactHash.class, name = "hash"),
        @JsonSubTypes.Type(value = ContactUnicity.class, name = "unicity"),
        @JsonSubTypes.Type(value = ContactId.class, name = "id"),
})
public abstract class Linked { }