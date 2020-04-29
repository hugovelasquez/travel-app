package com.example.sucaldotravelapp;

// I created this class to help me define a format in which to store retrieved database information.
// In this case, the User class format is built of seven Strings.

public class User {

    // Definition of variables
    private String startDateUser;
    private String endDateUser;
    private String fromCountryUser;
    private String fromCityUser;
    private String toCountryUser;
    private String toCityUser;
    private String tripDescUser;

    // Definition of User class
    public User(String sDate, String eDate, String fCountry, String fCity, String tCountry, String tCity, String tripDesc) {
        startDateUser = sDate;
        endDateUser = eDate;
        fromCountryUser = fCountry;
        fromCityUser = fCity;
        toCountryUser = tCountry;
        toCityUser = tCity;
        tripDescUser = tripDesc;
    }

    public String getStartDateUser() {
        return startDateUser;
    }

    public String getEndDateUser() {
        return endDateUser;
    }

    public String getFromCountryUser() {
        return fromCountryUser;
    }

    public String getFromCityUser() {
        return fromCityUser;
    }

    public String getToCountryUser() {
        return toCountryUser;
    }

    public String getToCityUser() {
        return toCityUser;
    }

    public String getTripDescUser() {
        return tripDescUser;
    }
}
