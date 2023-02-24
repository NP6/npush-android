package com.np6.npush.internal;

import com.np6.npush.internal.api.InteractionApi;
import com.np6.npush.internal.core.network.HttpClient;
import com.np6.npush.internal.core.network.driver.Driver;
import com.np6.npush.internal.models.action.TrackingAction;
import com.np6.npush.internal.models.common.Completion;
import com.np6.npush.internal.models.common.Result;


public class Interaction {

    public static void handle(TrackingAction<String> interaction, Completion<Object> completion) {
        try {

            Driver driver = new Driver(HttpClient.Create());

            InteractionApi api = new InteractionApi(driver);

            api.get(interaction.getRadical(), interaction.getValue(), completion::onComplete);

        } catch (Exception exception) {
            completion.onComplete(new Result.Error<>(exception));
        }
    }

}
