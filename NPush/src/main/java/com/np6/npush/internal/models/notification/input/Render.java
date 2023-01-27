package com.np6.npush.internal.models.notification.input;

import com.fasterxml.jackson.annotation.JsonCreator;
import com.fasterxml.jackson.annotation.JsonProperty;

import java.util.ArrayList;

public class Render {

    public String title;
    public String body;
    public String icon;
    public String thumbnail;
    public String image;
    public ArrayList buttons;

    @JsonCreator(mode = JsonCreator.Mode.PROPERTIES)
    public Render(
            @JsonProperty("title") String title,
            @JsonProperty("body") String body,
            @JsonProperty("icon") String icon,
            @JsonProperty("thumbnail") String thumbnail,
            @JsonProperty("image") String image,
            @JsonProperty("buttons") ArrayList buttons)

    {
        this.title = title;
        this.body = body;
        this.icon = icon;
        this.thumbnail = thumbnail;
        this.image = image;
        this.buttons = buttons;
    }


    public String getTitle() {
        return title;
    }

    public String getBody() {
        return body;
    }

    public String getIcon() {
        return icon;
    }
}
