package com.sucaldo.travelapp.model;

import android.database.Cursor;

public class CountriesContinents {

    private String continent;
    private String country;

    // Constructor for getting items out of db
    public CountriesContinents(Cursor data) {
        this.continent = data.getString(1);
        this.country = data.getString(2);
    }

    public String getContinent() {
        return continent;
    }

    public String getCountry() {
        return country;
    }
}
