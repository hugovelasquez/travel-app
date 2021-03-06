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
import androidx.fragment.app.FragmentResultListener;

import com.anychart.AnyChartView;
import com.anychart.charts.Cartesian;
import com.sucaldo.travelapp.R;
import com.sucaldo.travelapp.db.DatabaseHelper;
import com.sucaldo.travelapp.model.ChartData;
import com.sucaldo.travelapp.views.MainActivity;

import java.util.ArrayList;
import java.util.Calendar;
import java.util.List;


public class Top10PlacesFragment extends Fragment implements View.OnClickListener {

    private MainActivity activity;
    private ChartHelper chartHelper;
    private Cartesian barChart;
    private ChartData allYears, twoYears, fiveYears, tenYears;
    private DatabaseHelper myDB;

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        final View rootView = inflater.inflate(R.layout.charts_top_10_places_view, container, false);

        activity = (MainActivity) getActivity();
        myDB = new DatabaseHelper(getContext());
        chartHelper = new ChartHelper(myDB, getContext());

        RadioButton radioTop10All = rootView.findViewById(R.id.radio_top10_all);
        RadioButton radioTop102yrs = rootView.findViewById(R.id.radio_top10_2yrs);
        RadioButton radioTop105yrs = rootView.findViewById(R.id.radio_top10_5yrs);
        RadioButton radioTop1010yrs = rootView.findViewById(R.id.radio_top10_10yrs);
        radioTop10All.setOnClickListener(this);
        radioTop102yrs.setOnClickListener(this);
        radioTop105yrs.setOnClickListener(this);
        radioTop1010yrs.setOnClickListener(this);

        final AnyChartView top10CitiesChart = rootView.findViewById(R.id.top_10_places);

        ImageView returnToStatsIcon = rootView.findViewById(R.id.return_to_stats_icon);
        returnToStatsIcon.setOnClickListener(this);

        // Default case is show top10 overall
        radioTop10All.setChecked(true);

        // Get data from TripStaticsFragment.java
        getParentFragmentManager().setFragmentResultListener(getString(R.string.fragment_request_key_top10chart), this, new FragmentResultListener() {
            @Override
            public void onFragmentResult(@NonNull String key, @NonNull Bundle bundle) {
                allYears = bundle.getParcelable(getString(R.string.fragment_key_top10chart));
                barChart = chartHelper.initTop10PlacesChart(top10CitiesChart, true, allYears.getDataEntries());
            }
        });

        return rootView;
    }

    @Override
    public void onClick(View v) {

        switch (v.getId()) {
            case R.id.return_to_stats_icon:
                activity.goToStatistics();
                break;
            case R.id.radio_top10_all:
                chartHelper.updateChart(barChart, allYears.getDataEntries());
                break;
            case R.id.radio_top10_2yrs:
                if (twoYears == null) {
                    twoYears = new ChartData(myDB.getTop10VisitedPlaces(getLastNYears(2)));
                }
                chartHelper.updateChart(barChart, twoYears.getDataEntries());
                break;
            case R.id.radio_top10_5yrs:
                if (fiveYears == null) {
                    fiveYears = new ChartData(myDB.getTop10VisitedPlaces(getLastNYears(5)));
                }
                chartHelper.updateChart(barChart, fiveYears.getDataEntries());
                break;
            case R.id.radio_top10_10yrs:
                if (tenYears == null) {
                    tenYears = new ChartData(myDB.getTop10VisitedPlaces(getLastNYears(10)));
                }
                chartHelper.updateChart(barChart, tenYears.getDataEntries());
                break;
        }
    }

    private List<String> getLastNYears(int n) {
        int currentYear = Calendar.getInstance().get(Calendar.YEAR);
        List<String> years = new ArrayList<>();
        years.add(Integer.toString(currentYear));
        for (int i = 1; i <= n; i++) {
            int year = currentYear - i;
            years.add(Integer.toString(year));
        }
        return years;
    }

}
