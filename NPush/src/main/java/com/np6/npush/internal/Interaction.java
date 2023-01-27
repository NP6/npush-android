package com.np6.npush.internal;

import com.np6.npush.internal.api.InteractionApi;
import com.np6.npush.internal.models.action.TrackingAction;
import com.np6.npush.internal.models.common.Completion;
import com.np6.npush.internal.models.common.Result;


public class Interaction {

    public static void handle(TrackingAction<String> interaction, Completion<Object> completion) {
        try {

            InteractionApi api = new InteractionApi(interaction.getRadical());

            api.get(interaction.getValue(), completion::onComplete);

        } catch (Exception exception) {
            completion.onComplete(new Result.Error<>(exception));
        }
    }

}
