package com.np6.npush.internal.models.action;

public class TrackingAction<T> extends Action.InternalAction<T> {

    private final String radical;

    private final T value;

    public TrackingAction(T value, String radical) {
        super(value);
        this.value = value;
        this.radical = radical;
    }

    public String getRadical() {
        return radical;
    }

    public T getValue() {
        return value;
    }
}
