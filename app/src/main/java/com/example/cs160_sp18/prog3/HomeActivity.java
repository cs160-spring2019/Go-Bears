package com.example.cs160_sp18.prog3;

import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.widget.ScrollView;

import java.util.ArrayList;

public class HomeActivity extends AppCompatActivity {
    private static final String TAG = HomeActivity.class.getSimpleName();
    private String username;
    private RecyclerView mRecyclerView;
    private CardsAdapter mAdapter;
    private ArrayList<CardsView> mCardsView = new ArrayList<>();
   // private FusedLocationProviderClient fusedLocationClient;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_card_feed);
        mRecyclerView = findViewById(R.id.recycler);
        mRecyclerView.setHasFixedSize(true);
        mRecyclerView.setLayoutManager(new LinearLayoutManager(this));

        Intent intent = getIntent();
        username = intent.getStringExtra("username");


//        fusedLocationClient = LocationServices.getFusedLocationProviderClient(this);
        addBears();
    }

    private void addBears() {

        int imageUri = R.drawable.bell_bears;
        mCardsView.add(new CardsView(imageUri, "Bell Bears", "0 meters away"));

        mCardsView.add(new CardsView(R.drawable.bench_bears, "Bench Bears", "0 meters away"));

        mCardsView.add(new CardsView(R.drawable.in_stadium, "In Stadium", "0 meters away"));

        mCardsView.add(new CardsView(R.drawable.les_bears, "Les Bears", "0 meters away"));

        mCardsView.add(new CardsView(R.drawable.macchi_bears, "Macchi Bears", "0 meters away"));

        mCardsView.add(new CardsView(R.drawable.mlk_bear, "MLK Bears", "0 meters away"));

        mCardsView.add(new CardsView(R.drawable.outside_stadium, "Outside Stadium", "0 meters away"));

        mCardsView.add(new CardsView(R.drawable.south_hall, "South Hall", "0 meters away"));

        mCardsView.add(new CardsView(R.drawable.strawberry_creek, "Strawberry Creek", "0 meters away"));

        setAdapterAndUpdateData();

    }

    private void setAdapterAndUpdateData() {
        // create a new adapter with the updated mComments array
        // this will "refresh" our recycler view
        mAdapter = new CardsAdapter(this, mCardsView);
        mRecyclerView.setAdapter(mAdapter);
        mAdapter.setOnItemClickListener(new CardsAdapter.OnItemClickListener() {
            @Override
            public void onItemClick(int position) {
                CardsView card = mCardsView.get(position);
                startNewActivity(card.getTitle());


            }
        });
    }

    public void startNewActivity (String landmark) {

        Intent intent = new Intent (this, CommentFeedActivity.class);
        intent.putExtra("username", username);
        intent.putExtra("landmark", landmark);
        startActivity(intent);

    }
}


