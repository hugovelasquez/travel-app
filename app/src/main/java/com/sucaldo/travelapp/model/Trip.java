package com.sucaldo.travelapp.model;


import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;

public class Trip {

        private final String DATE_FORMAT_DB = "EEE MMM dd HH:mm:ss zzz yyyy";

        private int id;
        private String fromCountry;
        private String fromCity;
        private String toCountry;
        private String toCity;
        private String description;
        private Date startDate;
        private Date endDate;

        // Constructor for adding trips to database
        public Trip(String fromCountry, String fromCity, String toCountry, String toCity, String description, Date startDate, Date endDate) {
                this.fromCountry = fromCountry;
                this.fromCity = fromCity;
                this.toCountry = toCountry;
                this.toCity = toCity;
                this.description = description;
                this.startDate = startDate;
                this.endDate = endDate;
        }

        // Constructor for getting trips from database
        public Trip(String fromCountry, String fromCity, String toCountry, String toCity, String description, String startDate, String endDate, int id) {
            this.fromCountry = fromCountry;
            this.fromCity = fromCity;
            this.toCountry = toCountry;
            this.toCity = toCity;
            this.description = description;
            this.id = id;
            // Dates are stored as Strings in the database (Reminder: SQLite does not recognize type Date).
            // Try to parse the string from the database. If it does not work, as a "back-up plan" set current date
            // so that app does not crash.
            try {
                this.startDate = new SimpleDateFormat(DATE_FORMAT_DB).parse(startDate);
            }
            catch (ParseException e) {
                this.startDate = new Date();
            }
            try {
                this.endDate = new SimpleDateFormat(DATE_FORMAT_DB).parse(startDate);
            }
            catch (ParseException e) {
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
}
