package com.example.truck;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.NotificationCompat;
import androidx.core.app.NotificationManagerCompat;

import android.app.Notification;
import android.app.PendingIntent;
import android.content.Intent;
import android.location.Location;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import com.firebase.client.ChildEventListener;
import com.firebase.client.DataSnapshot;
import com.firebase.client.Firebase;
import com.firebase.client.FirebaseError;
import com.firebase.client.ValueEventListener;
import com.google.android.gms.maps.model.LatLng;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

public class Availableno extends AppCompatActivity {
    private Button mAvailable, mnotavailabe, macceptrequest;
    private FirebaseAuth mAuth;
    private FirebaseUser user;
    double latitude;
    double longitude;
    //double end_lat = 19.0236;
    //double end_lon = 72.8501;
    //float dist;
    //private DatabaseReference reference;
    private Firebase mRef;
    //private TextView latitude;
    //private TextView longitude;

    private NotificationManagerCompat notificationManagerCompat;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_availableno);

        notificationManagerCompat = NotificationManagerCompat.from(this);

        //latitude = (TextView) findViewById(R.id.latitude);
        //longitude = (TextView) findViewById(R.id.longitude);

        mAuth = FirebaseAuth.getInstance();
        user = mAuth.getCurrentUser();

        mAvailable = findViewById(R.id.available);
        mnotavailabe = findViewById(R.id.notavailable);
        macceptrequest = findViewById(R.id.mapbutton);


        mRef = new Firebase("https://truckpro-f3d30.firebaseio.com/Available/Sensor_ID_1/Motion_Detected");

        mRef.addChildEventListener(new ChildEventListener() {
            @Override
            public void onChildAdded(DataSnapshot dataSnapshot, String s) {

                if (dataSnapshot.child("latitude").getValue() != null) {
                    if (dataSnapshot.child("longitude").getValue() != null) {


                        latitude = dataSnapshot.child("latitude").getValue(Double.class);
                        longitude = dataSnapshot.child("longitude").getValue(Double.class);

                        LatLng loca = new LatLng(latitude, longitude);

//                        LatLng sydney = new LatLng(end_lat, end_lon);
//
                        Location loc1 = new Location("");
                        loc1.setLatitude(loca.latitude);
                        loc1.setLongitude(loca.longitude);

                        sendOnChannel1();
//
//                        Location loc2 = new Location("");
//                        loc2.setLatitude(sydney.latitude);
//                        loc2.setLongitude(sydney.longitude);
//
//                        dist = loc1.distanceTo(loc2);
//
//                        if (dist <= 54.2487) {
//                            sendOnChannel1();
//                        }
                    }
                } else {
                    if (dataSnapshot.child("latitude").getValue() == null) {
                        if (dataSnapshot.child("longitude").getValue() == null) {
//                            Toast.makeText(Availableno.this, "No request yet", Toast.LENGTH_LONG).show();
                            sendOnChannel1();
                        }
                    }
                }
            }

            @Override
            public void onChildChanged(DataSnapshot dataSnapshot, String s) {

            }

            @Override
            public void onChildRemoved(DataSnapshot dataSnapshot) {

            }

            @Override
            public void onChildMoved(DataSnapshot dataSnapshot, String s) {

            }

            @Override
            public void onCancelled(FirebaseError firebaseError) {

            }
        });
//        mRef.addValueEventListener(new ValueEventListener() {
//            @Override
//            public void onDataChange(DataSnapshot dataSnapshot) {
//
////                sendOnChannel1();
//                String value = dataSnapshot.getValue(String.class);
////                mValueView.setText(value);
//            }
//
//            @Override
//            public void onCancelled(FirebaseError firebaseError) {
//
//            }
//        });

        //reference = FirebaseDatabase.getInstance().getReferenceFromUrl("https://truckpro-f3d30.firebaseio.com/Available/Sensor_ID_1/Motion_Detected/latitude/");
                //.child(user.getUid()).child("Sensor_ID_1").child("Motion_Detected").child("latitude");
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

        mAvailable.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Available();
            }
        });

        mnotavailabe.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                NotAvailable();
            }
        });

        macceptrequest.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                acceptrequest();
//                Intent intent = new Intent(Availableno.this, notify.class);
//                startActivity(intent);
//                finish();
//                return;
            }
        });
    }

    private void acceptrequest() {

        DatabaseReference current_user = FirebaseDatabase.getInstance().getReference().child("Available").child("variableid");
                    current_user.setValue(user.getUid());

        mRef = new Firebase("https://truckpro-f3d30.firebaseio.com/Available/Sensor_ID_1/Motion_Detected");
        mRef.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {

                if (dataSnapshot.getValue() != null) {
                    Double latitude = dataSnapshot.child("latitude").getValue(Double.class);
                    Double longitude = dataSnapshot.child("longitude").getValue(Double.class);

                    Intent intent = new Intent(Availableno.this, DriverMapActivity.class);
                        startActivity(intent);
                        finish();
                        return;
                }
                else
                    {
                    Toast.makeText(getBaseContext(), "NULLL", Toast.LENGTH_SHORT).show();
                }
            }
//                Double latitude = dataSnapshot.child("latitude").getValue(Double.class);
//                Double longitude = dataSnapshot.child("longitude").getValue(Double.class);

//                double latitude1 = Double.parseDouble(latitude.getText().toString().trim());
//                double longitude1 = Double.parseDouble(longitude.getText().toString().trim());

            @Override
            public void onCancelled(FirebaseError firebaseError) {

            }
        });
    }

//    private void acceptrequest() {
//        DatabaseReference current_user = FirebaseDatabase.getInstance().getReference().child("Available").child("variableid");
//                    current_user.setValue(user.getUid());
//
//                    DatabaseReference sensorok = FirebaseDatabase.getInstance().getReference().child("Available").child("Sensor_ID_1").child("Motion_Detected");
//                    sensorok.child("latitude").setValue(19.0178);
//                    sensorok.child("longitude").setValue(72.8478);
//
//        Intent intent = new Intent(Availableno.this, DriverMapActivity.class);
//                startActivity(intent);
//                finish();
//                return;
//
//
//    }




    private void sendOnChannel1() {
        Intent yesIn = new Intent(this, DriverMapActivity.class);
        yesIn.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TASK);
        PendingIntent yes = PendingIntent.getActivity(this, 0, yesIn, PendingIntent.FLAG_ONE_SHOT);

        Intent noIn = new Intent(this, drivernotavailable.class);
        noIn.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_NEW_TASK);
        //creating a pending intent using the intent
        yesIn.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TASK);

        PendingIntent no = PendingIntent.getActivity(this, 0, noIn, PendingIntent.FLAG_ONE_SHOT);

        Notification notification = new NotificationCompat.Builder(this, App.CHANNEL_1_ID)
                .setSmallIcon(R.drawable.truck)
                .setContentTitle("Sewage Cleaning Alert Message")
                .setContentText("Overflow to be happen")
                .setPriority(NotificationCompat.PRIORITY_HIGH)
                .setCategory(NotificationCompat.CATEGORY_MESSAGE)
                .addAction(R.drawable.seeee, "yes", yes)
                .addAction(R.drawable.seeee, "no", no)
                .build();
        notificationManagerCompat.notify(1, notification);
    }




    private void Available() {

        DatabaseReference current_user = FirebaseDatabase.getInstance().getReferenceFromUrl("https://truckpro-f3d30.firebaseio.com/").child("Available").child("variableid");
        current_user.setValue(user.getUid());


        mRef = new Firebase("https://truckpro-f3d30.firebaseio.com/");
        mRef.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {

                String variableA = dataSnapshot.child("Available").child("variableid").getValue(String.class);
                String variableB = dataSnapshot.child("NotAvailable").child("variableid").getValue(String.class);

                if (dataSnapshot.getValue() != null) {
                    if (variableB != null) {
                        if (variableB.equals(variableA)) {

                            mRef.child("NotAvailable").child("variableid").setValue(null);
                            mRef.child("Available").child("variableid").setValue(user.getUid());

                        }
                    }
                }
            }

            @Override
            public void onCancelled(FirebaseError firebaseError) {

            }
        });


//        reference = FirebaseDatabase.getInstance().getReferenceFromUrl("https://truckpro-f3d30.firebaseio.com/");
//        reference.child("Available").child(user.getUid()).setValue("1");
    }

    private void NotAvailable() {

        DatabaseReference current_user = FirebaseDatabase.getInstance().getReferenceFromUrl("https://truckpro-f3d30.firebaseio.com/").child("NotAvailable").child("variableid");
        current_user.setValue(user.getUid());

        mRef = new Firebase("https://truckpro-f3d30.firebaseio.com/");
        mRef.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {

                String variableA = dataSnapshot.child("Available").child("variableid").getValue(String.class);
                String variableB = dataSnapshot.child("NotAvailable").child("variableid").getValue(String.class);

                    if (variableA != null) {
                        if (variableA.equals(variableB)) {

                            mRef.child("Available").child("variableid").setValue(null);
                            mRef.child("NotAvailable").child("variableid").setValue(user.getUid());

                        }
                    }
                }


            @Override
            public void onCancelled(FirebaseError firebaseError) {

            }
        });


//        reference = FirebaseDatabase.getInstance().getReferenceFromUrl("https://truckpro-f3d30.firebaseio.com/");
//        reference.child("NotAvailable").child(user.getUid()).setValue("0");
    }
}
