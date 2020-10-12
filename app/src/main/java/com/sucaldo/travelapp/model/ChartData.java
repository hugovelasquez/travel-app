package com.sucaldo.travelapp.model;

import android.os.Parcel;
import android.os.Parcelable;

import com.anychart.chart.common.dataentry.DataEntry;

import java.util.List;

// Parcelable allows to pass objects between fragments
public class ChartData implements Parcelable {

    private List<DataEntry> dataEntries;

    public ChartData(List<DataEntry> dataEntries) {
        this.dataEntries = dataEntries;
    }

    protected ChartData(Parcel in) {
    }

    public static final Creator<ChartData> CREATOR = new Creator<ChartData>() {
        @Override
        public ChartData createFromParcel(Parcel in) {
            return new ChartData(in);
        }

        @Override
        public ChartData[] newArray(int size) {
            return new ChartData[size];
        }
    };

    public List<DataEntry> getDataEntries() {
        return dataEntries;
    }

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
    }
}
