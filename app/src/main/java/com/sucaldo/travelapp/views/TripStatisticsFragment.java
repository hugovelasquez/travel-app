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
import com.sucaldo.travelapp.views.charts.ChartHelper;


public class TripStatisticsFragment extends Fragment implements View.OnClickListener {

    private MainActivity activity;
    private DatabaseHelper myDB;


    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        final View rootView = inflater.inflate(R.layout.trip_statistics_view, container, false);

        activity = (MainActivity) getActivity();
        myDB = new DatabaseHelper(getContext());

        ImageView expandTop10CitiesIcon = rootView.findViewById(R.id.expand_top_10_cities_icon);
        ImageView expandCountriesCloudIcon = rootView.findViewById(R.id.expand_countries_cloud_icon);

        expandCountriesCloudIcon.setOnClickListener(this);
        expandTop10CitiesIcon.setOnClickListener(this);

        AnyChartView top10CitiesChart = rootView.findViewById(R.id.stats_top_10_cities);
        AnyChartView countriesCloudChart = rootView.findViewById(R.id.stats_countries_cloud);

        ChartHelper chartHelper = new ChartHelper(myDB, getContext());
        chartHelper.initTop10CitiesChart(top10CitiesChart, false);
        chartHelper.initCountriesCloudChart(countriesCloudChart);

        return rootView;
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.expand_top_10_cities_icon:
                activity.goToTop10CitiesChart();
                break;
            case R.id.expand_countries_cloud_icon:
                activity.goToCountriesCloudChart();
                break;
        }
    }

}
