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
        this.city = data.getString(0);
        this.country = data.getString(1);
        this.latitude = data.getFloat(2);
        this.longitude = data.getFloat(3);
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
