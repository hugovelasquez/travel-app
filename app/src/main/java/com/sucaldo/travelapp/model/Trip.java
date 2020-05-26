package com.sucaldo.travelapp.model;


import java.util.Date;

public class Trip {

        private String fromCountry;
        private String fromCity;
        private String toCountry;
        private String toCity;
        private String description;
        private Date startDate;
        private Date endDate;

        public Trip(String fromCountry, String fromCity, String toCountry, String toCity, String description, Date startDate, Date endDate) {
                this.fromCountry = fromCountry;
                this.fromCity = fromCity;
                this.toCountry = toCountry;
                this.toCity = toCity;
                this.description = description;
                this.startDate = startDate;
                this.endDate = endDate;
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
}
