package com.sucaldo.travelapp.model;

// Class to display the grouping of trips by year in the list view
public class YearListItem {
    private int year;
    // booleans default value is always false
    private boolean expanded;

    public YearListItem(int year) {
        this.year = year;
    }

    public Integer getYear() {
        return year;
    }

    public boolean isExpanded() {
        return expanded;
    }

    public void setExpanded(boolean expanded) {
        this.expanded = expanded;
    }
}