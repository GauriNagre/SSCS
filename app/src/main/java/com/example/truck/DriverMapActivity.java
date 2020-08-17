package com.example.truck;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.ActivityCompat;
import androidx.fragment.app.FragmentActivity;

import android.Manifest;
import android.content.Context;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.graphics.Color;
import android.location.Address;
import android.location.Geocoder;
import android.location.Location;
import android.location.LocationListener;
import android.location.LocationManager;
import android.os.AsyncTask;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.Toast;

import com.firebase.client.Firebase;
import com.firebase.client.FirebaseError;
import com.firebase.client.utilities.Base64;
import com.google.android.gms.location.FusedLocationProviderClient;
import com.google.android.gms.location.LocationServices;
import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.OnMapReadyCallback;
import com.google.android.gms.maps.SupportMapFragment;
import com.google.android.gms.maps.model.BitmapDescriptorFactory;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.Marker;
import com.google.android.gms.maps.model.MarkerOptions;
import com.google.android.gms.maps.model.PolygonOptions;
import com.google.android.gms.maps.model.Polyline;
import com.google.android.gms.maps.model.PolylineOptions;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import org.json.JSONException;
import org.json.JSONObject;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.URL;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;


public class DriverMapActivity extends FragmentActivity implements OnMapReadyCallback {

    GoogleMap map;
    private DatabaseReference reference;
    private FirebaseAuth mAuth;
    private FirebaseUser user;
    private Firebase mRef;
    LocationManager locationManager;
    LocationListener locationListener;
    //Location currentLocation;
    //FusedLocationProviderClient fusedLocationProviderClient;
    Button route;
    Button taskcompleted;
    Polyline polyline = null;
    double latitude;
    double longitude;
    double end_lat = 19.0236;
    double end_lon = 72.8501;
    float dist;
    List<LatLng> listPoints = new ArrayList<>();
    List<Marker> markerList = new ArrayList<>();

    SupportMapFragment supportMapFragment;
    FusedLocationProviderClient client;

    private static final int REQUEST_CODE = 101;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_driver_map);

        supportMapFragment = (SupportMapFragment) getSupportFragmentManager()
                .findFragmentById(R.id.map);

        client = LocationServices.getFusedLocationProviderClient(this);

        if (ActivityCompat.checkSelfPermission(DriverMapActivity.this,
                Manifest.permission.ACCESS_FINE_LOCATION) == PackageManager.PERMISSION_GRANTED) {

            getCurrentLocation();
        } else {

            ActivityCompat.requestPermissions(DriverMapActivity.this,
                    new String[]{Manifest.permission.ACCESS_FINE_LOCATION}, 44);
        }


        route = findViewById(R.id.route);
        taskcompleted = findViewById(R.id.taskcompleted);

        final SupportMapFragment mapFragment = (SupportMapFragment) getSupportFragmentManager()
                .findFragmentById(R.id.map);
        mapFragment.getMapAsync(this);

        mAuth = FirebaseAuth.getInstance();
        user = mAuth.getCurrentUser();

//        locationManager = (LocationManager) getSystemService(LOCATION_SERVICE);
//        if (ActivityCompat.checkSelfPermission(this, Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED && ActivityCompat.checkSelfPermission(this, Manifest.permission.ACCESS_COARSE_LOCATION) != PackageManager.PERMISSION_GRANTED) {
//            ActivityCompat.requestPermissions(this, new String[]
//                    {Manifest.permission.ACCESS_FINE_LOCATION}, REQUEST_CODE);
////            return;
//            // TODO: Consider calling
//            //    Activity#requestPermissions
//            // here to request the missing permissions, and then overriding
//            //   public void onRequestPermissionsResult(int requestCode, String[] permissions,
//            //                                          int[] grantResults)
//            // to handle the case where the user grants the permission. See the documentation
//            // for Activity#requestPermissions for more details.
//            return;
//        }
//        if(locationManager.isProviderEnabled(LocationManager.NETWORK_PROVIDER)) {
//            locationManager.requestLocationUpdates(LocationManager.NETWORK_PROVIDER, 0, 0, new LocationListener() {
//                @Override
//                public void onLocationChanged(Location location) {
//                    double la = location.getLatitude();
//                    double lo = location.getLongitude();
//                    LatLng latLng = new LatLng(la,lo);
//                    Geocoder geocoder = new Geocoder(getApplicationContext());
//                    try {
//                        List<Address> addressList = geocoder.getFromLocation(la,lo,1);
//                        String str = addressList.get(0).getLocality()+",";
//                        str += addressList.get(0).getSubLocality();
//                        MarkerOptions markerOptions = new MarkerOptions().position(latLng).title(str);
//                        map.moveCamera(CameraUpdateFactory.newLatLngZoom(latLng,14F));
//                        Marker mar = map.addMarker(markerOptions);
//                        listPoints.add(latLng);
//                        markerList.add(mar);
//                    } catch (IOException e) {
//                        e.printStackTrace();
//                    }
//                }
//
//                @Override
//                public void onStatusChanged(String s, int i, Bundle bundle) {
//
//                }
//
//                @Override
//                public void onProviderEnabled(String s) {
//
//                }
//
//                @Override
//                public void onProviderDisabled(String s) {
//
//                }
//            });
//        }
//        else if(locationManager.isProviderEnabled(LocationManager.GPS_PROVIDER)){
//            locationManager.requestLocationUpdates(LocationManager.GPS_PROVIDER, 0, 0, new LocationListener() {
//                @Override
//                public void onLocationChanged(Location location) {
//                    double la = location.getLatitude();
//                    double lo = location.getLongitude();
//                    LatLng latLng = new LatLng(la,lo);
//                    Geocoder geocoder = new Geocoder(getApplicationContext());
//                    try {
//                        List<Address> addressList = geocoder.getFromLocation(la,lo,1);
//                        String str = addressList.get(0).getLocality()+",";
//                        str += addressList.get(0).getSubLocality();
//                        MarkerOptions markerOptions = new MarkerOptions().position(latLng).title(str);
//                        map.moveCamera(CameraUpdateFactory.newLatLngZoom(latLng,14F));
//                        Marker mar = map.addMarker(markerOptions);
//                        listPoints.add(latLng);
//                        markerList.add(mar);
//                    } catch (IOException e) {
//                        e.printStackTrace();
//                    }
//                }
//
//                @Override
//                public void onStatusChanged(String s, int i, Bundle bundle) {
//
//                }
//
//                @Override
//                public void onProviderEnabled(String s) {
//
//                }
//
//                @Override
//                public void onProviderDisabled(String s) {
//
//                }
//            });
//        }


//        fusedLocationProviderClient = LocationServices.getFusedLocationProviderClient(this);
//        fetchLastLocation();

        taskcompleted.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                mRef = new Firebase("https://truckpro-f3d30.firebaseio.com/Available");
                mRef.addValueEventListener(new com.firebase.client.ValueEventListener() {
                    @Override
                    public void onDataChange(com.firebase.client.DataSnapshot dataSnapshot) {

                        mRef.child("Sensor_ID_1").removeValue();
                        Toast.makeText(getApplicationContext(), "Task Completed", Toast.LENGTH_LONG).show();

                        Intent intent = new Intent(DriverMapActivity.this, Availableno.class);
                        startActivity(intent);
                        finish();
                        return;
                    }

                    @Override
                    public void onCancelled(FirebaseError firebaseError) {

                    }
                });


//                mRef = new Firebase("https://truckpro-f3d30.firebaseio.com/");
//                mRef.addValueEventListener(new com.firebase.client.ValueEventListener() {
//                    @Override
//                    public void onDataChange(com.firebase.client.DataSnapshot dataSnapshot) {
//
//                        String variableA = dataSnapshot.child("Available").child("variableid").getValue(String.class);
//                        String variableB = dataSnapshot.child("NotAvailable").child("variableid").getValue(String.class);
//
//                        if (dataSnapshot.getValue() != null) {
//                            if (variableA.equals(variableB)) {
//6
//                                mRef.child("NotAvailable").child("variableid").setValue(null);
//
//                            }
//                        }
//                    }

            }
        });

        route.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (polyline != null) polyline.remove();

                PolylineOptions polylineOptions = new PolylineOptions()
                        .addAll(listPoints).clickable(true).geodesic(true);
                polyline = map.addPolyline(polylineOptions);
            }
        });

    }

    private void getCurrentLocation() {

        if (ActivityCompat.checkSelfPermission(DriverMapActivity.this,Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED
                && ActivityCompat.checkSelfPermission(DriverMapActivity.this,Manifest.permission.ACCESS_COARSE_LOCATION) != PackageManager.PERMISSION_GRANTED) {
            // TODO: Consider calling
            //    Activity#requestPermissions
            // here to request the missing permissions, and then overriding
            //   public void onRequestPermissionsResult(int requestCode, String[] permissions,
            //                                          int[] grantResults)
            // to handle the case where the user grants the permission. See the documentation
            // for Activity#requestPermissions for more details.
            return;
        }
        Task<Location> task = client.getLastLocation();
        task.addOnSuccessListener(new OnSuccessListener<Location>() {
            @Override
            public void onSuccess(final Location location) {

                if(location != null){
                    supportMapFragment.getMapAsync(new OnMapReadyCallback() {
                        @Override
                        public void onMapReady(GoogleMap googleMap) {
                            LatLng latLng = new LatLng(location.getLatitude(),location.getLongitude());

                            MarkerOptions options = new MarkerOptions().position(latLng).title("Current location");

                            googleMap.animateCamera(CameraUpdateFactory.newLatLngZoom(latLng,14F));
                            googleMap.addMarker(options);
                            Marker mar = map.addMarker(options);
                            listPoints.add(latLng);
                            markerList.add(mar);
                        }
                    });
                }

            }
        });
    }

//    private void fetchLastLocation() {
//        if (ActivityCompat.checkSelfPermission(this, Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED && ActivityCompat.checkSelfPermission(this, Manifest.permission.ACCESS_COARSE_LOCATION) != PackageManager.PERMISSION_GRANTED) {
//            ActivityCompat.requestPermissions(this, new String[]
//                    {Manifest.permission.ACCESS_FINE_LOCATION}, REQUEST_CODE);
//            return;
//        }
//        Task<Location> task = fusedLocationProviderClient.getLastLocation();
//        task.addOnSuccessListener(new OnSuccessListener<Location>() {
//            @Override
//            public void onSuccess(Location location) {
//                if (location != null) {
//                    currentLocation = location;
//                    Toast.makeText(getApplicationContext(), currentLocation.getLatitude() + "" + currentLocation.getLongitude(), Toast.LENGTH_SHORT).show();
//
//                    SupportMapFragment mapFragment = (SupportMapFragment) getSupportFragmentManager()
//                            .findFragmentById(R.id.map);
//                    mapFragment.getMapAsync(DriverMapActivity.this);
//                }
//            }
//        });
//    }


    @Override
    public void onMapReady(GoogleMap googleMap) {
        map = googleMap;

        reference = FirebaseDatabase.getInstance().getReferenceFromUrl("https://truckpro-f3d30.firebaseio.com/Available").child("Sensor_ID_1").child("Motion_Detected");

        ValueEventListener listener = reference.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {

                latitude = dataSnapshot.child("latitude").getValue(Double.class);
                longitude = dataSnapshot.child("longitude").getValue(Double.class);

                LatLng loca = new LatLng(latitude, longitude);

                Marker marker1 = map.addMarker(new MarkerOptions().position(loca).title("Marker Location"));
                map.moveCamera(CameraUpdateFactory.newLatLngZoom(loca, 14F));
                listPoints.add(loca);
                markerList.add(marker1);


//                LatLng latLng = new LatLng(currentLocation.getLatitude(), currentLocation.getLongitude());
//                MarkerOptions markerOptions = new MarkerOptions().position(latLng)
//                        .title("I Am Here");
//                map.animateCamera(CameraUpdateFactory.newLatLng(latLng));
//                map.animateCamera(CameraUpdateFactory.newLatLngZoom(latLng, 14F));
//                Marker mar = map.addMarker(markerOptions);
//                listPoints.add(latLng);
//                markerList.add(mar);

                getCurrentLocation();



                LatLng sydney = new LatLng(end_lat,end_lon);
                MarkerOptions markerOptions = new MarkerOptions().position(sydney).title("Driver 1 location");
                //map.addMarker(new MarkerOptions().position(sydney).title("Marker in Sydney"));
                map.moveCamera(CameraUpdateFactory.newLatLng(sydney));
                map.animateCamera(CameraUpdateFactory.newLatLngZoom(sydney,14F));
                Marker mar = map.addMarker(markerOptions);
                listPoints.add(sydney);
                markerList.add(mar);

//                Toast.makeText(DriverMapActivity.this,"Driver has been aloted for the task" ,Toast.LENGTH_LONG).show();

//                Location loc1 = new Location("");
//                loc1.setLatitude(loca.latitude);
//                loc1.setLongitude(loca.longitude);
//
//                Location loc2 = new Location("");
//                loc2.setLatitude(sydney.latitude);
//                loc2.setLongitude(sydney.longitude);
//
//                dist = loc1.distanceTo(loc2);
//                Toast.makeText(DriverMapActivity.this,"Driver allotted  " + dist,Toast.LENGTH_LONG).show();

        }
            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {
            }
        });
    }

    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        switch (requestCode) {
            case REQUEST_CODE:
                if (grantResults.length > 0 && grantResults[0] == PackageManager.PERMISSION_GRANTED) {
                    getCurrentLocation();
                }
                break;
        }
    }
        }























