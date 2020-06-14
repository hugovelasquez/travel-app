package com.sucaldo.travelapp.model;


import android.database.Cursor;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;

public class Trip {

    private final String DATE_FORMAT_DB = "EEE MMM dd HH:mm:ss zzz yyyy";
    private final String DATE_FORMAT_PRETTY = "dd.MM.yy";
    private final String DATE_FORMAT_PICKER = "d.M.yyyy";

    private int id;
    private int groupId;
    private String fromCountry;
    private String fromCity;
    private String toCountry;
    private String toCity;
    private String description;
    private Date startDate;
    private Date endDate;

    // Constructor for adding trips to database
    public Trip(String fromCountry, String fromCity, String toCountry, String toCity, String description, Date startDate, Date endDate, int groupId) {
        this.fromCountry = fromCountry;
        this.fromCity = fromCity;
        this.toCountry = toCountry;
        this.toCity = toCity;
        this.description = description;
        this.startDate = startDate;
        this.endDate = endDate;
        this.groupId = groupId;
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
        // Dates are stored as Strings in the database (Reminder: SQLite does not recognize type Date).
        // Try to parse the string from the database. If it does not work, as a "back-up plan" set current date
        // so that app does not crash.
        try {
            this.startDate = new SimpleDateFormat(DATE_FORMAT_DB).parse(data.getString(6));
        } catch (ParseException e) {
            this.startDate = new Date();
        }
        try {
            this.endDate = new SimpleDateFormat(DATE_FORMAT_DB).parse(data.getString(7));
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

    public void setGroupId(int groupId) {
        this.groupId = groupId;
    }

    public String getFormattedStartDate() {
        return formatDate(startDate, DATE_FORMAT_PRETTY);
    }

    public String getFormattedEndDate() {
        return formatDate(endDate, DATE_FORMAT_PRETTY);
    }

    public String getPickerFormattedStartDate() {
        return formatDate(startDate, DATE_FORMAT_PICKER);
    }

    public String getPickerFormattedEndDate() {
        return formatDate(endDate, DATE_FORMAT_PICKER);
    }

    private String formatDate(Date date, String format) {
        SimpleDateFormat dateFormat = new SimpleDateFormat(format);
        return dateFormat.format(date);
    }
}
