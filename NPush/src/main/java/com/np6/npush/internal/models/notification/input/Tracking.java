package com.np6.npush.internal.models.notification.input;

import com.fasterxml.jackson.annotation.JsonCreator;
import com.fasterxml.jackson.annotation.JsonProperty;
import com.np6.npush.internal.models.action.TrackingAction;

public class Tracking {

    public String impression;
    public String dismiss;
    public String redirection;
    public String radical;

    @JsonCreator
    public Tracking(
            @JsonProperty("impression") String impression,
            @JsonProperty("dismiss") String dismiss,
            @JsonProperty("redirection") String redirection,
            @JsonProperty("radical") String radical
            )
    {
        this.impression = impression;
        this.dismiss = dismiss;
        this.redirection = redirection;
        this.radical = radical;
    }

    public String getRedirection() {
        return redirection;
    }

    public String getDismiss() {
        return dismiss;
    }

    public String getImpression() {
        return impression;
    }

    public String getRadical() {
        return radical;
    }

    public TrackingAction<String> getImpressionAction() {
        return new TrackingAction<>(this.getImpression(), this.getRadical() );
    }

}
