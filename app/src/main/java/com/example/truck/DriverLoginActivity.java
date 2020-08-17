package com.example.truck;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.NotificationCompat;
import androidx.core.app.NotificationManagerCompat;

import android.app.Notification;
import android.app.PendingIntent;
import android.content.Intent;
import android.os.Bundle;
import android.util.Patterns;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import com.firebase.client.Firebase;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseAuthUserCollisionException;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

public class DriverLoginActivity extends AppCompatActivity  {

    private EditText mEmail, mPassword;
    private Button mLogin, mRegistration;
   // private Button mAvailable, mnotavailabe;

    private NotificationManagerCompat notificationManagerCompat;

    private FirebaseAuth mAuth;
    private FirebaseUser user;
    private DatabaseReference reference;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_driver_login);

        //notificationManagerCompat = NotificationManagerCompat.from(this);

        mAuth = FirebaseAuth.getInstance();
        user = mAuth.getCurrentUser();


        mEmail = findViewById(R.id.email);
        mPassword = findViewById(R.id.password);

        mLogin = findViewById(R.id.login);
        mRegistration = findViewById(R.id.registration);
       // mAvailable = findViewById(R.id.available);
       // mnotavailabe = findViewById(R.id.notavailable);

//        reference = FirebaseDatabase.getInstance().getReferenceFromUrl("https://truckpro-f3d30.firebaseio.com/").child("Available")
//                .child(user.getUid()).child("Sensor_ID_1").child("Motion_Detected").child("latitude");
//        reference.addValueEventListener(new ValueEventListener() {
//            @Override
//            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
//                sendOnChannel1();
//            }
//
//            @Override
//            public void onCancelled(@NonNull DatabaseError databaseError) {
//            }
//        });

        mLogin.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                userLogin();
            }
        });

        mRegistration.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                registerUser();
            }
        });

//        mAvailable.setOnClickListener(new View.OnClickListener() {
//            @Override
//            public void onClick(View view) {
//                Available();
//            }
//        });
//
//        mnotavailabe.setOnClickListener(new View.OnClickListener() {
//            @Override
//            public void onClick(View view) {
//                NotAvailable();
//            }
//        });
    }

//    private void sendOnChannel1() {
//        Intent yesIn = new Intent(this, notify.class);
//        yesIn.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TASK);
//        PendingIntent yes = PendingIntent.getActivity(this, 0, yesIn, PendingIntent.FLAG_ONE_SHOT);
//
//        Intent noIn = new Intent(this, notify.class);
//        //creating a pending intent using the intent
//        yesIn.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TASK);
//
//        PendingIntent no = PendingIntent.getActivity(this, 0, yesIn, PendingIntent.FLAG_ONE_SHOT);
//
//        Notification notification = new NotificationCompat.Builder(this, App.CHANNEL_1_ID)
//                .setSmallIcon(R.drawable.truck)
//                .setContentTitle("Sewage Cleaning Alert Message")
//                .setContentText("Overflow to be happen")
//                .setPriority(NotificationCompat.PRIORITY_HIGH)
//                .setCategory(NotificationCompat.CATEGORY_MESSAGE)
//                .addAction(R.drawable.truck, "yes", yes)
//                .addAction(R.drawable.truck, "no", no)
//                .build();
//        notificationManagerCompat.notify(1, notification);
//    }


//    private void Available() {
//        String email = mEmail.getText().toString().trim();
//        String password = mPassword.getText().toString().trim();
//
//        if (email.isEmpty()) {
//            mEmail.setError("Email is required");
//            mEmail.requestFocus();
//            return;
//        }
//
//        if (password.isEmpty()) {
//            mPassword.setError("Password is required");
//            mPassword.requestFocus();
//            return;
//        }
//
//        if (password.length() < 6) {
//            mPassword.setError("Minimum length of password should be 6");
//            mPassword.requestFocus();
//            return;
//        }
//
//        DatabaseReference current_user = FirebaseDatabase.getInstance().getReferenceFromUrl("https://truckpro-f3d30.firebaseio.com/").child("Drivers").child(user.getUid()).child("Available").child("name");
//        current_user.setValue(email);
//
//        //String id = reference.push().getKey();
////        reference = FirebaseDatabase.getInstance().getReferenceFromUrl("https://truckpro-f3d30.firebaseio.com/").child("Driver");
////        //Available available = new Available(id);
////        reference.child("Available").child(user.getUid()).setValue("name");
//    }
//
//    private void NotAvailable() {
//        String email = mEmail.getText().toString().trim();
//        String password = mPassword.getText().toString().trim();
//
//        if (email.isEmpty()) {
//            mEmail.setError("Email is required");
//            mEmail.requestFocus();
//            return;
//        }
//
//        if (password.isEmpty()) {
//            mPassword.setError("Password is required");
//            mPassword.requestFocus();
//            return;
//        }
//
//        if (password.length() < 6) {
//            mPassword.setError("Minimum length of password should be 6");
//            mPassword.requestFocus();
//            return;
//        }
//
//        DatabaseReference current_user = FirebaseDatabase.getInstance().getReferenceFromUrl("https://truckpro-f3d30.firebaseio.com/").child("Drivers").child(user.getUid()).child("NotAvailable").child("name");
//        current_user.setValue(email);
//
////        reference = FirebaseDatabase.getInstance().getReferenceFromUrl("https://truckpro-f3d30.firebaseio.com/").child("Driver");
////        reference.child("NotAvailable").child(user.getUid()).setValue("0");
//    }

    private void userLogin() {
        String email = mEmail.getText().toString().trim();
        String password = mPassword.getText().toString().trim();

        if (email.isEmpty()) {
            mEmail.setError("Email is required");
            mEmail.requestFocus();
            return;
        }

        if (password.isEmpty()) {
            mPassword.setError("Password is required");
            mPassword.requestFocus();
            return;
        }

        if (password.length() < 6) {
            mPassword.setError("Minimum length of password should be 6");
            mPassword.requestFocus();
            return;
        }

        mAuth.signInWithEmailAndPassword(email, password).addOnCompleteListener(new OnCompleteListener<AuthResult>() {
            @Override
            public void onComplete(@NonNull Task<AuthResult> task) {
                if (task.isSuccessful()) {
//                    String email = mEmail.getText().toString().trim();
//                    String password = mPassword.getText().toString().trim();

//                    DatabaseReference current_user = FirebaseDatabase.getInstance().getReference().child("Drivers").child(user.getUid()).child("name");
//                    current_user.setValue(email);
//
//                    DatabaseReference sensorok = FirebaseDatabase.getInstance().getReference().child("Drivers").child(user.getUid()).child("Available").child("name").child("Sensor_ID_1").child("motion_sensor_status");
//                    sensorok.child("latitude").setValue(19.169257);
//                    sensorok.child("longitude").setValue(73.341601);
                    finish();
                    Intent intent = new Intent(DriverLoginActivity.this, Availableno.class);
                    intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
                    startActivity(intent);
                    Toast.makeText(getApplicationContext(), "logged in", Toast.LENGTH_LONG).show();
                } else {
                    Toast.makeText(getApplicationContext(), task.getException().getMessage(), Toast.LENGTH_SHORT).show();
                }
            }
        });
    }

    private void registerUser() {
        String email = mEmail.getText().toString().trim();
        String password = mPassword.getText().toString().trim();

        if (email.isEmpty()) {
            mEmail.setError("Email is required");
            mEmail.requestFocus();
            return;
        }

        if (password.isEmpty()) {
            mPassword.setError("Password is required");
            mPassword.requestFocus();
            return;
        }

        if (password.length() < 6) {
            mPassword.setError("Minimum lenght of password should be 6");
            mPassword.requestFocus();
            return;
        }


        mAuth.createUserWithEmailAndPassword(email, password).addOnCompleteListener(new OnCompleteListener<AuthResult>() {
            @Override
            public void onComplete(@NonNull Task<AuthResult> task) {

                if (task.isSuccessful()) {
//                    String email = mEmail.getText().toString().trim();
//                    String password = mPassword.getText().toString().trim();
                    // String user_id = mAuth.getCurrentUser().getUid();
                    Toast.makeText(getApplicationContext(), "Registered Sucessfully " + user.getUid(), Toast.LENGTH_SHORT).show();

//                    DatabaseReference current_user = FirebaseDatabase.getInstance().getReference().child("Drivers").child(user.getUid()).child("name");
//                    current_user.setValue(email);
//
//                    DatabaseReference sensorok = FirebaseDatabase.getInstance().getReference().child("Drivers").child(user.getUid()).child("Available").child("name").child("Sensor_ID_1").child("motion_sensor_status");
//                    sensorok.child("latitude").setValue(19.169257);
//                    sensorok.child("longitude").setValue(73.341601);
                } else {

                    if (task.getException() instanceof FirebaseAuthUserCollisionException) {
                        Toast.makeText(getApplicationContext(), "You are already registered", Toast.LENGTH_SHORT).show();

                    } else {
                        Toast.makeText(getApplicationContext(), task.getException().getMessage(), Toast.LENGTH_SHORT).show();
                    }

                }
            }
        });

    }


    @Override
    protected void onStart() {
        super.onStart();
    }

    @Override
    protected void onStop() {
        super.onStop();

    }

}


