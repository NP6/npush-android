package com.np6.npush.internal.models.notification.input;

import com.fasterxml.jackson.annotation.JsonCreator;
import com.fasterxml.jackson.annotation.JsonProperty;
import com.np6.npush.internal.models.action.TrackingAction;

public class Tracking {

    public String impression;
    public String dismiss;
    public String redirection;
    public String radical;
    public Optout optout;

    @JsonCreator
    public Tracking(
            @JsonProperty("impression") String impression,
            @JsonProperty("dismiss") String dismiss,
            @JsonProperty("redirection") String redirection,
            @JsonProperty("radical") String radical,
            @JsonProperty("optout") Optout optout
            )
    {
        this.impression = impression;
        this.dismiss = dismiss;
        this.redirection = redirection;
        this.optout = optout;
        this.radical = radical;

    }

    public String getChannelOptout() { return optout.getChannel(); }

    public String getGlobalOptout() { return optout.getGlobal(); }

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

    public TrackingAction<String> getChannelOptoutAction() {
        return new TrackingAction<>(this.getChannelOptout(), this.getRadical());
    }

    public TrackingAction<String> getGlobalOptoutAction() {
        return new TrackingAction<>(this.getGlobalOptout(), this.getRadical());
    }
}
