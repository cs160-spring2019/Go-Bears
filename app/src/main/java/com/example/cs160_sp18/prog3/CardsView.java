package com.example.cs160_sp18.prog3;

import android.net.Uri;

public class CardsView {
    private int imageUri;
    private String title;
    private String distance;

    public CardsView(int imageUri, String title, String distance) {
        this.imageUri = imageUri;
        this.title = title;
        this.distance = distance;
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

    public String getDistance() {
        return distance;
    }

    public void setDistance(String distance) {
        this.distance = distance;
    }
}
