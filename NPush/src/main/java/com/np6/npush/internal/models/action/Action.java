package com.np6.npush.internal.models.action;

public abstract class Action {


    public static class UserAction<T> extends Action {

    }

    public static class InternalAction<T> extends Action {

        public final T value;

        public InternalAction(T value) {
            this.value = value;
        }
    }

}
