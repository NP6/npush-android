package com.np6.npush.internal.models.contact;

import com.fasterxml.jackson.annotation.JsonCreator;
import com.fasterxml.jackson.annotation.JsonProperty;

public class ContactId extends Linked {

    public String value;

    @JsonCreator(mode = JsonCreator.Mode.PROPERTIES)
    public ContactId(@JsonProperty("value") String value) {
        this.value = value;
    }
}