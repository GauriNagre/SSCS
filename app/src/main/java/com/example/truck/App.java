package com.example.truck;

import android.app.NotificationChannel;
import android.app.NotificationManager;
import android.os.Build;
import android.os.Bundle;
import android.app.Application;

import com.firebase.client.Firebase;

public class App extends Application {
    public static final String CHANNEL_1_ID ="Channel1";

    public void onCreate(){
        super.onCreate();

        createNotificationchannels();
        Firebase.setAndroidContext(this);
    }

    private void createNotificationchannels() {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O){
            NotificationChannel channel1 = new NotificationChannel(
                    CHANNEL_1_ID,
                     "Channel 1",
                    NotificationManager.IMPORTANCE_HIGH
            );
            channel1.setDescription("This is channel1");

            NotificationManager manager = getSystemService(NotificationManager.class);
            manager.createNotificationChannel(channel1);

        }
    }
}

