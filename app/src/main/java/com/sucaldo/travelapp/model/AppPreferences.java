package com.sucaldo.travelapp.model;

import android.content.Context;
import android.content.SharedPreferences;

import com.sucaldo.travelapp.R;
import com.sucaldo.travelapp.db.DatabaseHelper;

public class AppPreferences {

    private SharedPreferences sharedPreferences;
    private DatabaseHelper myDB;
    private String countryKey, cityKey;

    public AppPreferences(Context context, DatabaseHelper databaseHelper) {
        this.myDB = databaseHelper;
        countryKey = context.getString(R.string.home_country_key);
        cityKey = context.getString(R.string.home_city_key);
        sharedPreferences = context.getSharedPreferences(context.getString(R.string.preferences_id),
                Context.MODE_PRIVATE);
    }

    public void storeCountrySelection(String countrySelection) {
        SharedPreferences.Editor editor = sharedPreferences.edit();
        editor.putString(countryKey, countrySelection);
        editor.apply();
    }

    public void storeCitySelection(String citySelection) {
        SharedPreferences.Editor editor = sharedPreferences.edit();
        editor.putString(cityKey, citySelection);
        editor.apply();
    }

    public String getStoredCountrySelection() {
        return sharedPreferences.getString(countryKey, null);
    }

    public String getStoredCitySelection() {
        return sharedPreferences.getString(cityKey, null);
    }

    public boolean isHomeLocationPresent() {
        return sharedPreferences.contains(countryKey)
                && sharedPreferences.contains(cityKey);
    }

    public CityLocation getSavedHomeLocation() {
        String country = getStoredCountrySelection();
        String city = getStoredCitySelection();
        return myDB.getLocationOfCity(country, city);
    }

}
