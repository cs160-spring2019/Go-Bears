package com.example.cs160_sp18.prog3;

import android.Manifest;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.IntentSender;
import android.content.pm.PackageManager;
import android.location.Location;
import android.os.Bundle;
import android.os.Looper;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.app.ActivityCompat;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.widget.Button;

import com.google.android.gms.common.api.ResolvableApiException;
import com.google.android.gms.location.FusedLocationProviderClient;
import com.google.android.gms.location.LocationCallback;
import com.google.android.gms.location.LocationRequest;
import com.google.android.gms.location.LocationResult;
import com.google.android.gms.location.LocationServices;
import com.google.android.gms.location.LocationSettingsRequest;
import com.google.android.gms.location.LocationSettingsResponse;
import com.google.android.gms.location.SettingsClient;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;

import java.util.ArrayList;
import java.util.List;

public class HomeActivity extends AppCompatActivity {


    private String username;
    private RecyclerView mRecyclerView;
    private CardsAdapter mAdapter;
    private Location lastLocation;
    private Button mUpdateLocation;
    private LocationCallback locationCallback;
    private LocationRequest mLocationRequest;
    private ArrayList<CardsView> mCardsView = new ArrayList<>();
    private FusedLocationProviderClient fusedLocationClient;
    private boolean requestingLocationUpdates = true;
    protected static final int REQUEST_CHECK_SETTINGS = 0x1;
    public static final int MY_PERMISSIONS_REQUEST_LOCATION = 99;
    protected final static String REQUESTING_LOCATION_UPDATES_KEY = "requesting-location-updates-key";
    private boolean first = true;


    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_card_feed);
        Intent intent = getIntent();
        username = intent.getStringExtra("username");
        mRecyclerView = findViewById(R.id.recycler);
        mRecyclerView.setHasFixedSize(true);
        mRecyclerView.setLayoutManager(new LinearLayoutManager(this));
        mUpdateLocation = findViewById(R.id.update_location);
        addBears();

        fusedLocationClient = LocationServices.getFusedLocationProviderClient(this);

        //askPermission();
        locationCallback = new LocationCallback() {
            @Override
            public void onLocationResult(LocationResult locationResult) {
                if (locationResult == null) {
                    return;
                }
                List<Location> locs = locationResult.getLocations();
                lastLocation = locs.get(locs.size() - 1);
                if (first) {
                    updateDistance();
                    first = false;
                }
            };
        };

        //buildLocationRequest();
        startLocationUpdates();

        mUpdateLocation.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                updateDistance();

            }

        });

    }

    private void addBears() {
        Location loc = createLocation("Bell Bears", 37.872061599999995, 122.2578123);
        int dist;

        mCardsView.add(new CardsView(R.drawable.bell_bears, "Bell Bears", loc, 0));

        loc = createLocation("Bench Bears", 37.87233810000001, 122.25792999999999);
      //  dist = Math.round(loc.distanceTo(lastLocation));
        mCardsView.add(new CardsView(R.drawable.bench_bears, "Bench Bears", loc, 0));

        loc = createLocation("Les Bears", 37.871707, 122.253602);
   //     dist = Math.round(loc.distanceTo(lastLocation));
        mCardsView.add(new CardsView(R.drawable.les_bears, "Les Bears", loc, 0));

        loc = createLocation("Macchi Bears", 37.874118, 122.258778);
   //     dist = Math.round(loc.distanceTo(lastLocation));
        mCardsView.add(new CardsView(R.drawable.macchi_bears, "Macchi Bears", loc, 0));

        loc = createLocation("MLK Bears", 37.869288, 122.260125);
   //     dist = Math.round(loc.distanceTo(lastLocation));
        mCardsView.add(new CardsView(R.drawable.mlk_bear, "MLK Bears", loc, 0));

        loc = createLocation("Outside Stadium", 37.871305, 122.252516);
   //     dist = Math.round(loc.distanceTo(lastLocation));
        mCardsView.add(new CardsView(R.drawable.outside_stadium, "Outside Stadium", loc, 0));

        loc = createLocation("South Hall", 37.871382, 122.258355);
    //    dist = Math.round(loc.distanceTo(lastLocation));
        mCardsView.add(new CardsView(R.drawable.south_hall, "South Hall", loc, 0));

        loc = createLocation("Strawberry Creek", 37.869861, 122.261148);
     //   dist = Math.round(loc.distanceTo(lastLocation));
        mCardsView.add(new CardsView(R.drawable.strawberry_creek, "Strawberry Creek", loc, 0));

        setAdapterAndUpdateData();

    }

    private void updateDistance() {
        for (int i = 0; i < mCardsView.size(); i++) {
            CardsView c = mCardsView.get(i);
            c.setDistance((int) Math.round(c.getLocation().distanceTo(lastLocation)));
        }
        mAdapter.notifyDataSetChanged();

    }

    private Location createLocation(String name, double lat, double lon) {
        Location location = new Location(name);
        location.setLongitude(lon);
        location.setLatitude(lat);
        return location;
    }

    private void setAdapterAndUpdateData() {
        mAdapter = new CardsAdapter(this, mCardsView);
        mRecyclerView.setAdapter(mAdapter);
        mAdapter.setOnItemClickListener(new CardsAdapter.OnItemClickListener() {
            @Override
            public void onItemClick(int position) {
                CardsView card = mCardsView.get(position);
                if (card.getDistance() <= 10) {
                    startNewActivity(card.getTitle());
                } else {
                    promptDialog2(card.getTitle());
                }
            }
        });
    }

    public void startNewActivity(String landmark) {

        Intent intent = new Intent(this, CommentFeedActivity.class);
        intent.putExtra("username", username);
        intent.putExtra("landmark", landmark);
        startActivity(intent);

    }

    public void askPermission() {
        ActivityCompat.requestPermissions(HomeActivity.this,
                new String[]{Manifest.permission.ACCESS_FINE_LOCATION},
                MY_PERMISSIONS_REQUEST_LOCATION);

        if (ActivityCompat.checkSelfPermission(this, Manifest.permission.ACCESS_FINE_LOCATION)
                != PackageManager.PERMISSION_GRANTED) {

            String message = "If location services are disabled, it won't be able to track your location." +
                    " Are you sure you want to proceed?";
            new AlertDialog.Builder(this)
                    .setMessage(message)
                    .setNegativeButton("No", new DialogInterface.OnClickListener() {
                        @Override
                        public void onClick(DialogInterface dialogInterface, int i) {
                            //Prompt the user once explanation has been shown
                            ActivityCompat.requestPermissions(HomeActivity.this,
                                    new String[]{Manifest.permission.ACCESS_FINE_LOCATION},
                                    MY_PERMISSIONS_REQUEST_LOCATION);
                        }
                    }).setPositiveButton("Yes", null)
                    .create()
                    .show();
        }

        fusedLocationClient.getLastLocation().addOnSuccessListener(this,
                new OnSuccessListener<Location>() {
                    @Override
                    public void onSuccess(Location location) {
                        // Got last known location. In some rare situations this can be null.
                        if (location != null) {
                            // Logic to handle location object
                            lastLocation = location;
                        }
                    }
                });

    }

    private void promptDialog2(String title) {
        AlertDialog.Builder builder = new AlertDialog.Builder(this);
        builder.setMessage("Access Denied! You have to be within 10 meters from " + title+ " to gain access.")
                .setCancelable(false)
                .setPositiveButton("OK", new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialog, int id) {
                        //do things
                    }
                });
        AlertDialog alert = builder.create();
        alert.show();

    }

    private void buildLocationRequest() {
        mLocationRequest = new LocationRequest();
        mLocationRequest.setPriority(LocationRequest.PRIORITY_HIGH_ACCURACY);
        mLocationRequest.setInterval(5000);
        mLocationRequest.setFastestInterval(300);
    }

    private void startLocationUpdates() {
        buildLocationRequest();
        ActivityCompat.requestPermissions(HomeActivity.this,
                new String[]{Manifest.permission.ACCESS_FINE_LOCATION},
                MY_PERMISSIONS_REQUEST_LOCATION);

        if (ActivityCompat.checkSelfPermission(this, Manifest.permission.ACCESS_FINE_LOCATION)
                != PackageManager.PERMISSION_GRANTED) {

            String message = "If location services are disabled, it won't be able to track your location." +
                    " Are you sure you want to proceed?";
            new AlertDialog.Builder(this)
                    .setMessage(message)
                    .setNegativeButton("No", new DialogInterface.OnClickListener() {
                        @Override
                        public void onClick(DialogInterface dialogInterface, int i) {
                            //Prompt the user once explanation has been shown
                            ActivityCompat.requestPermissions(HomeActivity.this,
                                    new String[]{Manifest.permission.ACCESS_FINE_LOCATION},
                                    MY_PERMISSIONS_REQUEST_LOCATION);
                        }
                    }).setPositiveButton("Yes", null)
                    .create()
                    .show();
        }

        fusedLocationClient.requestLocationUpdates(mLocationRequest,
                locationCallback,
                Looper.myLooper() /* Looper */);
    }


}




