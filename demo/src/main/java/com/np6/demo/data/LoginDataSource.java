package com.np6.demo.data;

import android.content.Context;
import android.os.StrictMode;

import com.np6.demo.data.model.LoggedInUser;
import com.np6.npush.NPush;
import com.np6.npush.internal.models.contact.ContactHash;

import org.json.JSONObject;

import java.io.IOException;

import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.Response;

/**
 * Class that handles authentication w/ login credentials and retrieves user information.
 */
public class LoginDataSource {

    public Result login(Context context, String username, String password) {

            // TODO: handle loggedInUser authentication
            StrictMode.ThreadPolicy policy = new StrictMode.ThreadPolicy.Builder().permitAll().build();
            StrictMode.setThreadPolicy(policy);

            OkHttpClient client = new OkHttpClient();
            try {


                LoggedInUser fakeUser =
                        new LoggedInUser(
                                java.util.UUID.randomUUID().toString(),
                                "Utilisateur");

                return new Result.Success<>(fakeUser);
            } catch (Exception e) {
                return new Result.Error(new IOException("Error logging in", e));
            }
    }

    public void logout() {
        // TODO: revoke authentication
    }
}