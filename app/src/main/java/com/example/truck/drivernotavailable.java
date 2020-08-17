package com.example.truck;

import androidx.appcompat.app.AppCompatActivity;

import android.app.Application;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;

import com.firebase.client.Firebase;

public class drivernotavailable extends AppCompatActivity {
    private Button goback;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_drivernotavailable);

        goback = (Button) findViewById(R.id.goback);

        goback.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(drivernotavailable.this, Availableno.class);
                startActivity(intent);
                finish();
                return;
            }
        });
    }

    public static class FireApp extends Application {

        @Override
        public void onCreate() {
            super.onCreate();

            Firebase.setAndroidContext(this);
        }
    }
}
