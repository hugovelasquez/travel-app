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
import com.sucaldo.travelapp.views.charts.Top10CitiesChartHelper;


public class TripStatisticsFragment extends Fragment implements View.OnClickListener {

    private MainActivity activity;
    private DatabaseHelper myDB;


    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        final View rootView = inflater.inflate(R.layout.trip_statistics_view, container, false);

        activity = (MainActivity) getActivity();

        myDB = new DatabaseHelper(getContext());

        ImageView expandTop10CitiesBtn = rootView.findViewById(R.id.expand_top_10_cities_icon);
        expandTop10CitiesBtn.setOnClickListener(this);

        AnyChartView anyChartView = rootView.findViewById(R.id.stats_top_10_cities);
        new Top10CitiesChartHelper().initChart(anyChartView);
        anyChartView.setOnClickListener(this);

        return rootView;
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.expand_top_10_cities_icon:
                activity.goTop10Cities();
                break;
        }
    }

}
