package com.sucaldo.travelapp.model;

import android.database.Cursor;

public class CityLocation {

    private int id;
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
    public CityLocation(Cursor data){
        this.latitude = data.getFloat(0);
        this.longitude = data.getFloat(1);
    }


    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getCountry() {
        return country;
    }

    public void setCountry(String country) {
        this.country = country;
    }

    public String getCity() {
        return city;
    }

    public void setCity(String city) {
        this.city = city;
    }

    public float getLatitude() {
        return latitude;
    }

    public void setLatitude(float latitude) {
        this.latitude = latitude;
    }

    public float getLongitude() {
        return longitude;
    }

    public void setLongitude(float longitude) {
        this.longitude = longitude;
    }
}
