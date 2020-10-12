package com.sucaldo.travelapp.views;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;

import com.anychart.AnyChartView;
import com.sucaldo.travelapp.R;
import com.sucaldo.travelapp.db.DatabaseHelper;
import com.sucaldo.travelapp.model.ChartData;
import com.sucaldo.travelapp.views.charts.ChartHelper;

import java.util.Collections;


public class TripStatisticsFragment extends Fragment implements View.OnClickListener {

    private MainActivity activity;
    private ChartData top10Data, countryCloudData, areaChartData, bubbleChartData;


    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        final View rootView = inflater.inflate(R.layout.trip_statistics_view, container, false);

        activity = (MainActivity) getActivity();
        DatabaseHelper myDB = new DatabaseHelper(getContext());

        ImageView expandTop10PlacesIcon = rootView.findViewById(R.id.expand_top_10_places_icon);
        ImageView expandCountriesCloudIcon = rootView.findViewById(R.id.expand_countries_cloud_icon);
        ImageView expandKmsAreaChartIcon = rootView.findViewById(R.id.expand_kms_area_chart_icon);
        ImageView expandKmsBubbleChartIcon = rootView.findViewById(R.id.expand_kms_bubble_chart_icon);

        expandCountriesCloudIcon.setOnClickListener(this);
        expandTop10PlacesIcon.setOnClickListener(this);
        expandKmsAreaChartIcon.setOnClickListener(this);
        expandKmsBubbleChartIcon.setOnClickListener(this);

        AnyChartView top10PlacesChart = rootView.findViewById(R.id.stats_top_10_places);
        AnyChartView countriesCloudChart = rootView.findViewById(R.id.stats_countries_cloud);
        AnyChartView kmsAreaChart = rootView.findViewById(R.id.stats_kms_area_chart);
        AnyChartView kmsBubbleChart = rootView.findViewById(R.id.stats_kms_bubble_chart);

        // Data must be stored in parcelable objects so that in can be passed between fragments
        top10Data = new ChartData(myDB.getTop10VisitedPlaces(Collections.<String>emptyList()));
        countryCloudData = new ChartData(myDB.getVisitedCountries());
        areaChartData = new ChartData(myDB.getKmsPerContinentPerYear());
        bubbleChartData = new ChartData(myDB.getKmsAndTripsPerYear());

        ChartHelper chartHelper = new ChartHelper(myDB, getContext());
        chartHelper.initTop10PlacesChart(top10PlacesChart, false, top10Data.getDataEntries());
        chartHelper.initCountriesCloudChart(countriesCloudChart, false, countryCloudData.getDataEntries());
        chartHelper.initKmsAreaChart(kmsAreaChart, false, areaChartData.getDataEntries());
        chartHelper.initKmsBubbleChart(kmsBubbleChart, false, bubbleChartData.getDataEntries());


        return rootView;
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.expand_top_10_places_icon:
                activity.goToTop10CitiesChart(top10Data);
                break;
            case R.id.expand_countries_cloud_icon:
                activity.goToCountriesCloudChart(countryCloudData);
                break;
            case R.id.expand_kms_area_chart_icon:
                activity.goToKmsAreaChart(areaChartData);
                break;
            case R.id.expand_kms_bubble_chart_icon:
                activity.goToKmsBubbleChart(bubbleChartData);
                break;
        }
    }

}
