package com.np6.npush.internal.models.contact;

import com.fasterxml.jackson.annotation.JsonCreator;
import com.fasterxml.jackson.annotation.JsonProperty;

public class ContactHash extends Linked {

    public String value;

    @JsonCreator(mode = JsonCreator.Mode.PROPERTIES)
    public ContactHash(@JsonProperty("value") String value) {
        this.value = value;
    }
}