package com.np6.npush;

import java.util.UUID;

public class Config {

    public UUID application;
    public String identity;
    public String defaultChannelId;
    public boolean telemetry = false;

    public Config(UUID application, String identity, String defaultChannelId, boolean telemetry) {
        this.application = application;
        this.identity = identity;
        this.defaultChannelId = defaultChannelId;
        this.telemetry = telemetry;
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
    public boolean isTelemetryActivated() {return telemetry; }

}
