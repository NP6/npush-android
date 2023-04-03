package com.np6.npush.internal.models.log.android;

import android.os.Build;

import com.fasterxml.jackson.annotation.JsonProperty;

public class Android extends Device {

    @JsonProperty("release")
    public String release = Build.VERSION.RELEASE;

    @JsonProperty("device")
    public String device = Build.DEVICE;

    @JsonProperty("model")
    public String model = Build.MODEL;

    @JsonProperty("sdk")
    public int sdk = android.os.Build.VERSION.SDK_INT;

    @JsonProperty("product")
    public String product = Build.PRODUCT;

}
