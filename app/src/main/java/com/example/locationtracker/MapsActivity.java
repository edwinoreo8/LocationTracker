package com.example.locationtracker;

import android.Manifest;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.content.pm.PackageManager;
import android.graphics.Color;
import android.location.Location;
import android.os.Build;
import android.os.Bundle;
import android.os.SystemClock;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.Chronometer;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;

import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.OnMapReadyCallback;
import com.google.android.gms.maps.SupportMapFragment;
import com.google.android.gms.maps.model.BitmapDescriptorFactory;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.Marker;
import com.google.android.gms.maps.model.MarkerOptions;
import com.google.android.gms.maps.model.Polyline;
import com.google.android.gms.maps.model.PolylineOptions;

import java.math.RoundingMode;
import java.text.DecimalFormat;

import static java.lang.Math.abs;

public class MapsActivity extends AppCompatActivity implements OnMapReadyCallback {
    SupportMapFragment mapFragment;
    //All My MAP variables
    LatLng latLng;
    LatLng startingPos;
    LatLng prev;
    Polyline line;
    GoogleMap mMap;
    Marker marker;
    Marker endMarker;
    int start = 0;
    double totDistance = 0.00;
    //
    //All My Button and text fields
    Button[] btn = new Button[2];
    Chronometer chronometer;
    EditText distDisplay;
    long pauseOffset;
     boolean running;
     boolean stopped = false;



    LocationBroadcastReceiver receiver;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_maps);
        receiver = new LocationBroadcastReceiver();
        if (Build.VERSION.SDK_INT >= 23) {
            if (checkSelfPermission(Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED) {
                requestPermissions(new String[]{Manifest.permission.ACCESS_FINE_LOCATION}, 1);

            }
            Log.d("test", "PERMISSION GRANTED");
        }
        //Getting Map Fragments
        mapFragment = (SupportMapFragment) getSupportFragmentManager().findFragmentById(R.id.mapFrag);
        mapFragment.getMapAsync(this);
        //Setting our buttons so we can do actions later on
        btn[0] = (Button) findViewById(R.id.startBtn);
        btn[1]  = (Button) findViewById(R.id.endBtn);
        btn[1].setClickable(false);
        //Setting Our Timer for our Run
        chronometer = findViewById(R.id.timer);
        chronometer.setFormat("Time: %s");
        chronometer.setBase(SystemClock.elapsedRealtime());
        //Setting our Edit text here to what shows in screen
        distDisplay = findViewById(R.id.disTxt);

    }
    //We are sending this activity to another activity (LocRequest) to acquire GPS tracking from the phone
    void startLocService() {
        Log.d("TEST", "STARTLOCSERVICES ENTETRD");
        IntentFilter filter = new IntentFilter("ACT_LOC");
        registerReceiver(receiver, filter);
        Intent intent = new Intent(MapsActivity.this, LocRequest.class);
        startService(intent);
    }
    //This is checking to see if the user has accepted our request for GPS, if no is clicked the user will have to exit and come back so we can ask again
    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);
        switch (requestCode) {
            case 1:
                if (grantResults[0] == PackageManager.PERMISSION_GRANTED) {
                    //startLocService();
                    //startLocService();
                } else {
                    Toast.makeText(this, "Give me permissions", Toast.LENGTH_LONG).show();
                }
        }
    }
    //This function adds an event if the Start button is clicked or the finish button is clicked
    public void onClick(View view){
        int id = view.getId();
        if(id == R.id.startBtn ){
            startLocService();
          btn[1].setClickable(true);
            //Running starts at false when start button begins the clock starts
            if (!running) {
                chronometer.setBase(SystemClock.elapsedRealtime() - pauseOffset);
                chronometer.start();
                running = true;
                stopped = false;
            }

        }else{
            //Finish button is clicked Stop the clock and add alerts make sure if the user wants to finish the run
            stopped = true;
            if(stopped){
                chronometer.stop();
                pauseOffset = SystemClock.elapsedRealtime() - chronometer.getBase();
                running = false;
            }

            if(btn[1].isClickable() == true) {

                AlertDialog.Builder builder = new AlertDialog.Builder(this);
                builder.setTitle("Continue Run?");
                String[] choices = {"Yes", "No"};
                builder.setItems(choices, (dialog, which) -> {
                    if (which == 0) {
                        onClick(findViewById(R.id.startBtn));
                    } else {
                        long time = chronometer.getBase();
                        AlertDialog.Builder build = new AlertDialog.Builder(this);
                        build.setTitle("Great Run!");
                        String[] choice = {"Finish"};
                        build.setItems(choice, (dialog1, which1) -> {
                            if (which1 == 0) {
                                finish();
                            }
                        });
                        AlertDialog alertDialog = build.create();
                        alertDialog.show();
                    }
                });
                AlertDialog alertDialog = builder.create();
                alertDialog.show();
            }
        }
    }
    //Making the map fragment
    @Override
    public void onMapReady(GoogleMap googleMap) {
        mMap = googleMap;
    }
    //If the app is paused stop the clock
    @Override
    protected void onPause() {
        super.onPause();
        if(running){
            chronometer.stop();
            pauseOffset = SystemClock.elapsedRealtime() - chronometer.getBase();
            running = false;
        }

    }
    //This is the main part of this activity
    public class LocationBroadcastReceiver extends BroadcastReceiver {
        @Override
        public void onReceive(Context context, Intent intent) {

            //If the program hasnt been stopped go about getting the Lat n lang positions
          if(stopped == false) {

              if (intent.getAction().equals("ACT_LOC")) {
                  double lat = intent.getDoubleExtra("latitude", 0f);
                  double longitude = intent.getDoubleExtra("longitude", 0f);

                  if (start == 0) {
                      startingPos = new LatLng(lat, longitude);
                      prev = startingPos;
                      start++;
                  }
                  //If there is an end marker from the user clicking end but decided continue his run deleted marker
                  if(endMarker !=null){
                      mMap.clear();
                      endMarker = null;
                      marker =null;
                  }
                    // Place a marker in the map of your position
                  if (mMap != null) {
                      latLng = new LatLng(lat, longitude);
                      MarkerOptions markerOptions = new MarkerOptions();
                      markerOptions.position(latLng);
                      if (marker != null)
                          marker.setPosition(latLng);
                      else
                          marker = mMap.addMarker(markerOptions);
                      //marker.setPosition(latLng);
                      mMap.animateCamera(CameraUpdateFactory.newLatLngZoom(latLng, 19));
                  }

                    //This is just for testing and checking value
                  Log.d("MYLOG", "------RESTART OF CALL--------");
                  Log.d("MYLOG", "This is PREV Point:" + prev.latitude + "," + prev.longitude);
                  Log.d("MYLOG", "This is CURRENT Point:" + latLng.latitude + "," + latLng.longitude);
                  //

                  //This Section of the Code Has the previous Lat n Lang and the current Lat n Lang we then add up the distance between those to get the total distance of the run.
                  totDistance += distance(prev.latitude, prev.longitude, latLng.latitude, latLng.longitude);
                  DecimalFormat df = new DecimalFormat("#.##");
                  String distance;
                  //Lat n  lang gives us a total of 10 decimals spaces we only want to see X.XX so we formatted it and placed it so we can view it
                  if(totDistance >= 0.01){
                      Log.d("Test", "TOT DISTANCE IS ENTERED");
                      distance = df.format(totDistance);
                      distDisplay.setText(distance + " Miles");
                  }else{
                      Log.d("Test", "TOT DISTANCE ELSE IS ENTERED");
                      distDisplay.setText("0.00 Miles" );
                  }
                  prev = latLng;
                  //
                  //Toast.makeText(MapsActivity.this, "Latitude is: " + lat + ", Longitude is " + longitude, Toast.LENGTH_LONG).show();
                 // Log.d("MYLOG", "This is starting Point:" + startingPos.latitude + "," + startingPos.longitude);
                 // Toast.makeText(MapsActivity.this, "THIS IS TOTAL DISTANCE TRAVELED IN MILES" + totDistance, Toast.LENGTH_LONG).show();
              }
          }else{//If the program has been stopped place a marker in the first location and in the very last and route between them
              marker.setPosition(startingPos);
                endMarker = mMap.addMarker(new MarkerOptions().position(latLng).icon(BitmapDescriptorFactory.defaultMarker(BitmapDescriptorFactory.HUE_GREEN)));
              endMarker.setPosition(latLng);
              if (line != null) {
                  line.remove();
              }
              line = mMap.addPolyline(new PolylineOptions().add(startingPos, latLng).width(5).color(Color.BLUE));
          }
        }
    }

    //This function takes in Latitude and longitude values, we then do the correct maths to convert them into distances of miles for our running app.
    private double distance(double lat1, double lng1, double lat2, double lng2){

        double earthRadius = 3958.75; // in miles

        double deltaLat = Math.toRadians(lat2-lat1);
        double deltaLon = Math.toRadians(lng2-lng1);

        double sineLat = Math.sin(deltaLat / 2);
        double sineLon = Math.sin(deltaLon / 2);

        double intermRslt = Math.pow(sineLat, 2) + Math.pow(sineLon, 2)
                * Math.cos(Math.toRadians(lat1)) * Math.cos(Math.toRadians(lat2));

        double angle = 2 * Math.atan2(Math.sqrt(intermRslt), Math.sqrt(1-intermRslt));

        double dist = earthRadius * angle;

        return dist; // output distance, in MILES
    }
}
