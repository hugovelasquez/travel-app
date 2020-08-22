package com.sucaldo.travelapp.views.charts;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.RadioButton;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;

import com.anychart.AnyChartView;
import com.anychart.charts.Cartesian;
import com.sucaldo.travelapp.R;
import com.sucaldo.travelapp.db.DatabaseHelper;
import com.sucaldo.travelapp.views.MainActivity;

import java.util.ArrayList;
import java.util.Calendar;
import java.util.Collections;
import java.util.List;


public class Top10PlacesFragment extends Fragment implements View.OnClickListener {

    private MainActivity activity;
    private ChartHelper chartHelper;
    private Cartesian barChart;

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        final View rootView = inflater.inflate(R.layout.charts_top_10_places_view, container, false);

        activity = (MainActivity) getActivity();
        DatabaseHelper myDB = new DatabaseHelper(getContext());
        chartHelper = new ChartHelper(myDB, getContext());

        RadioButton radioTop10All = rootView.findViewById(R.id.radio_top10_all);
        RadioButton radioTop102yrs = rootView.findViewById(R.id.radio_top10_2yrs);
        RadioButton radioTop105yrs = rootView.findViewById(R.id.radio_top10_5yrs);
        RadioButton radioTop1010yrs = rootView.findViewById(R.id.radio_top10_10yrs);
        radioTop10All.setOnClickListener(this);
        radioTop102yrs.setOnClickListener(this);
        radioTop105yrs.setOnClickListener(this);
        radioTop1010yrs.setOnClickListener(this);

        AnyChartView top10CitiesChart = rootView.findViewById(R.id.top_10_places);
        barChart = chartHelper.initTop10PlacesChart(top10CitiesChart, true);

        ImageView returnToStatsIcon = rootView.findViewById(R.id.return_to_stats_icon);
        returnToStatsIcon.setOnClickListener(this);

        // Default case is show top10 overall
        radioTop10All.setChecked(true);

        return rootView;
    }

    @Override
    public void onClick(View v) {

        switch (v.getId()) {
            case R.id.return_to_stats_icon:
                activity.goToStatistics();
                break;
            case R.id.radio_top10_all:
                chartHelper.updateChart(barChart, Collections.<String>emptyList());
                break;
            case R.id.radio_top10_2yrs:
                chartHelper.updateChart(barChart, getLastNYears(2));
                break;
            case R.id.radio_top10_5yrs:
                chartHelper.updateChart(barChart, getLastNYears(5));
                break;
            case R.id.radio_top10_10yrs:
                chartHelper.updateChart(barChart, getLastNYears(10));
                break;
        }
    }

    private List<String> getLastNYears(int n) {
        Integer currentYear = Calendar.getInstance().get(Calendar.YEAR);
        List<String> years = new ArrayList<>();
        years.add(currentYear.toString());
        for (int i = 1; i <= n; i++) {
            Integer year = currentYear - i;
            years.add(year.toString());
        }
        return years;
    }

}
