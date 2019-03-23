package com.example.cs160_sp18.prog3;

import android.location.Location;
import android.net.Uri;

public class CardsView {
    private int imageUri;
    private Location location;
    private String title;
    private int distance;

    public CardsView(int imageUri, String title, Location location, int distance) {
        this.imageUri = imageUri;
        this.title = title;
        this.distance = distance;
        this.location = location;
    }

    public int getImageUri() {
        return imageUri;
    }

    public void setImageUri(int imageUri) {
        this.imageUri = imageUri;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public int getDistance() {
        return distance;
    }
    public Location getLocation(){
        return this.location;
    }

    public void setDistance(int distance) {
        this.distance = distance;
    }
}
