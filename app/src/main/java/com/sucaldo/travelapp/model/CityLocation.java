package com.sucaldo.travelapp.model;

import android.database.Cursor;

public class CityLocation {

    private String country;
    private String city;
    private float latitude;
    private float longitude;

    public CityLocation(String country, String city, float latitude, float longitude) {
        this.country = country;
        this.city = city;
        this.latitude = latitude;
        this.longitude = longitude;
    }

    // Constructor for getting location out of database
    public CityLocation(Cursor data) {
        this.latitude = data.getFloat(0);
        this.longitude = data.getFloat(1);
    }


    public String getCountry() {
        return country;
    }

    public String getCity() {
        return city;
    }

    public float getLatitude() {
        return latitude;
    }

    public float getLongitude() {
        return longitude;
    }

}
