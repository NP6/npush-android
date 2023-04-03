package com.np6.demo;

import android.app.NotificationChannel;
import android.app.NotificationManager;
import android.app.TaskStackBuilder;
import android.content.Intent;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;

import com.google.android.material.snackbar.Snackbar;

import androidx.appcompat.app.AppCompatActivity;

import android.view.View;

import androidx.navigation.NavController;
import androidx.navigation.Navigation;
import androidx.navigation.ui.AppBarConfiguration;
import androidx.navigation.ui.NavigationUI;

import com.np6.demo.databinding.ActivityMainBinding;
import com.np6.npush.Config;
import com.np6.npush.NPush;
import com.np6.npush.internal.models.contact.ContactHash;
import com.np6.npush.internal.models.contact.ContactId;

import java.util.UUID;

public class MainActivity extends AppCompatActivity {

    private AppBarConfiguration appBarConfiguration;
    private ActivityMainBinding binding;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

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
                UUID.fromString("c5d6993f-7e81-4039-8755-5b82694bf473"),
                "MCOM032",
                "marketingChannel",
                true
        );
        NPush.Instance().setConfig(config);

        NPush.Instance().initialize(this);

        NPush.Instance().setInterceptor((context, deeplink) -> {
            TaskStackBuilder stackBuilder = TaskStackBuilder.create(context);

            Intent intentMain = new Intent(context, MainActivity.class);
            stackBuilder.addNextIntent(intentMain);

            Intent intent = new Intent(context, ProductsActivity.class);
            stackBuilder.addNextIntent(intent);

            Uri uri = Uri.parse(deeplink);
            Intent deeplinkIntent = new Intent(Intent.ACTION_VIEW)
                    .addFlags(Intent.FLAG_ACTIVITY_NEW_TASK)
                    .setData(uri);
            stackBuilder.addNextIntent(deeplinkIntent);
            return stackBuilder;
        });

        try {
            Thread.sleep(800);
        } catch (InterruptedException e) {
            e.printStackTrace();
        }

        NPush.Instance().setContact(this, new ContactId("000T39KL"));

        binding = ActivityMainBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());

        setSupportActionBar(binding.toolbar);

        NavController navController = Navigation.findNavController(this, R.id.nav_host_fragment_content);
        appBarConfiguration = new AppBarConfiguration.Builder(navController.getGraph()).build();
        NavigationUI.setupActionBarWithNavController(this, navController, appBarConfiguration);

        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
            String CHANNEL_ID = "Customer channel id";
            CharSequence name = "Customer channel name";
            String description = "Customer channel description";
            int importance = NotificationManager.IMPORTANCE_DEFAULT;
            NotificationChannel channel = new NotificationChannel(CHANNEL_ID, name, importance);
            channel.setDescription(description);
            // Register the channel with the system; you can't change the importance
            // or other notification behaviors after this
            NotificationManager notificationManager = getSystemService(NotificationManager.class);
            notificationManager.createNotificationChannel(channel);
        }

        binding.fab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Snackbar.make(view, "Replace with your own action", Snackbar.LENGTH_LONG)
                        .setAction("Action", null).show();
            }
        });
    }


    @Override
    public boolean onSupportNavigateUp() {
        NavController navController = Navigation.findNavController(this, R.id.nav_host_fragment_content);
        return NavigationUI.navigateUp(navController, appBarConfiguration)
                || super.onSupportNavigateUp();
    }
}