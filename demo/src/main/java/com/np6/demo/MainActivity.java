package com.np6.demo;

import androidx.appcompat.app.AppCompatActivity;

import android.app.NotificationChannel;
import android.app.NotificationManager;
import android.app.TaskStackBuilder;
import android.content.Context;
import android.content.Intent;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.os.StrictMode;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import com.np6.npush.Config;
import com.np6.npush.NPush;
import com.np6.npush.internal.models.DeeplinkInterceptor;
import com.np6.npush.internal.models.contact.ContactHash;
import com.np6.npush.internal.models.contact.ContactId;
import com.np6.npush.internal.models.contact.Linked;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.UUID;

import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.Response;

public class MainActivity extends AppCompatActivity {


    @Override
    protected void onNewIntent(Intent intent) {
        super.onNewIntent(intent);
        setIntent(intent);


        Toast.makeText(this, "Deeplink handled " + intent.getData(), Toast.LENGTH_LONG).show();

        showIntentDeeplink(intent);
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);


        final Button button = findViewById(R.id.button);
        button.setOnClickListener(v -> {

            try {
                final TextView contactValue = (TextView) findViewById(R.id.editText_linked);

                JSONObject jsonObject = fetchContactData(contactValue.getText().toString());

                String id = jsonObject.getString("id");

                NPush.Instance().setContact(this, new ContactId(id));

                showContact(jsonObject);

            } catch (JSONException ignored) {

            }

        });


        String marketingChannel = "marketingChannel";
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {

            String name = marketingChannel;
            String description = "Description du channel markteting";
            int importance = NotificationManager.IMPORTANCE_DEFAULT;
            NotificationChannel channel = new NotificationChannel(name, name, importance);
            channel.setDescription(description);
            // Register the channel with the system. You can't change the importance
            // or other notification behaviors after this.
            NotificationManager notificationManager = getSystemService(NotificationManager.class);
            notificationManager.createNotificationChannel(channel);
        }


        Config config = new Config(
                UUID.fromString(BuildConfig.NP6_APPLICATION),
                BuildConfig.NP6_AGENCY,
                "marketingChannel",
                true
        );
        NPush.Instance().setConfig(config);

        NPush.Instance().initialize(this);

        showIntentDeeplink(getIntent());
    }

    private void showIntentDeeplink(Intent intent) {

        final TextView deeplinkTextView = (TextView) findViewById(R.id.textView_deeplink);

        deeplinkTextView.setText("Deeplink : " + intent.getData());

        Toast.makeText(this, "Deeplink handled " + intent.getData(), Toast.LENGTH_LONG).show();


    }

    private void showContact(JSONObject object) {
        try {
            final TextView contactTextView = (TextView) findViewById(R.id.textView_contact);
            contactTextView.setText(" \n Contact Id: " + object.getString("id"));
        } catch (Exception exception) {
            Toast.makeText(this, "Unable to load contact. Reason : " + exception.getLocalizedMessage(), Toast.LENGTH_SHORT).show();
        }
    }

    private JSONObject fetchContactData(String username) {

        StrictMode.ThreadPolicy policy = new StrictMode.ThreadPolicy.Builder().permitAll().build();
        StrictMode.setThreadPolicy(policy);

        OkHttpClient client = new OkHttpClient();

        try {
            Request request = new Request.Builder()
                    .url(BuildConfig.NP6_TARGET_API + username)
                    .addHeader("X-Key", BuildConfig.NP6_TARGET_API_KEY)
                    .build();

            Response response = client.newCall(request).execute();

            String body = response.body().string();

            JSONObject object = new JSONObject(body);

            return object;
        } catch (Exception e) {
            Toast.makeText(this, "Unable to load contact. Reason : " + e.getLocalizedMessage(), Toast.LENGTH_SHORT).show();
            return new JSONObject();
        }

    }

}