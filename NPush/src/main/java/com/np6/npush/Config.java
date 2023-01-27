package com.np6.npush;

import java.util.UUID;

public class Config {

    public UUID application;
    public String identity;
    public String defaultChannelId;

    public Config(UUID application, String identity, String defaultChannelId) {
        this.application = application;
        this.identity = identity;
        this.defaultChannelId = defaultChannelId;
    }

    public String getIdentity() {
        return identity;
    }
    public String getDefaultChannel() {
        return defaultChannelId;
    }
    public UUID getApplication() {
        return application;
    }
}
