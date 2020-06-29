package com.sucaldo.travelapp.model;


import android.database.Cursor;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Locale;

public class Trip implements Comparable<Trip>{

    private int id;
    private int groupId;
    private String fromCountry;
    private String fromCity;
    private String toCountry;
    private String toCity;
    private String description;
    private Date startDate;
    private Date endDate;
    private long distance;

    // Constructor for adding trips to database
    public Trip(String fromCountry, String fromCity, String toCountry, String toCity, String description,
                Date startDate, Date endDate, int groupId, long distance) {
        this.fromCountry = fromCountry;
        this.fromCity = fromCity;
        this.toCountry = toCountry;
        this.toCity = toCity;
        this.description = description;
        this.startDate = startDate;
        this.endDate = endDate;
        this.groupId = groupId;
        this.distance = distance;
    }

    // Constructor for getting trip out of database
    public Trip(Cursor data) {
        this.fromCountry = data.getString(1);
        this.fromCity = data.getString(2);
        this.toCountry = data.getString(3);
        this.toCity = data.getString(4);
        this.description = data.getString(5);
        this.id = data.getInt(0);
        this.groupId = data.getInt(8);
        this.distance = data.getLong(9);
        // Dates are stored as Strings in the database (Reminder: SQLite does not recognize type Date).
        // Try to parse the string from the database. If it does not work, as a "back-up plan" set current date
        // so that app does not crash.
        try {
            this.startDate = new SimpleDateFormat(DateFormat.DB, Locale.getDefault()).parse(data.getString(6));
        } catch (ParseException e) {
            this.startDate = new Date();
        }
        try {
            this.endDate = new SimpleDateFormat(DateFormat.DB, Locale.getDefault()).parse(data.getString(7));
        } catch (ParseException e) {
            this.endDate = new Date();
        }
    }

    public String getFromCountry() {
        return fromCountry;
    }

    public void setFromCountry(String fromCountry) {
        this.fromCountry = fromCountry;
    }

    public String getFromCity() {
        return fromCity;
    }

    public void setFromCity(String fromCity) {
        this.fromCity = fromCity;
    }

    public String getToCountry() {
        return toCountry;
    }

    public void setToCountry(String toCountry) {
        this.toCountry = toCountry;
    }

    public String getToCity() {
        return toCity;
    }

    public void setToCity(String toCity) {
        this.toCity = toCity;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public Date getStartDate() {
        return startDate;
    }

    public void setStartDate(Date startDate) {
        this.startDate = startDate;
    }

    public Date getEndDate() {
        return endDate;
    }

    public void setEndDate(Date endDate) {
        this.endDate = endDate;
    }

    public int getId() {
        return id;
    }

    public int getGroupId() {
        return groupId;
    }

    public long getDistance() {
        return distance;
    }

    public void setDistance(long distance) {
        this.distance = distance;
    }

    public String getFormattedStartDate() {
        return formatDate(startDate, DateFormat.PRETTY);
    }

    public String getFormattedEndDate() {
        return formatDate(endDate, DateFormat.PRETTY);
    }

    public String getPickerFormattedStartDate() {
        return formatDate(startDate, DateFormat.PICKER);
    }

    public String getPickerFormattedEndDate() {
        return formatDate(endDate, DateFormat.PICKER);
    }

    private String formatDate(Date date, String format) {
        SimpleDateFormat dateFormat = new SimpleDateFormat(format, Locale.getDefault());
        return dateFormat.format(date);
    }

    @Override
    public int compareTo(Trip o) {
        if (this.getStartDate().before(o.getStartDate())) {
            return -1;
        }
        if (this.getStartDate().after(o.getStartDate())) {
            return 1;
        }
        return 0;
    }
}
