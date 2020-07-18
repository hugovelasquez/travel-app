package com.sucaldo.travelapp.views.charts;

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
import com.sucaldo.travelapp.views.MainActivity;


public class CountriesCloudFragment extends Fragment implements View.OnClickListener {

    private MainActivity activity;
    private DatabaseHelper myDB;

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        final View rootView = inflater.inflate(R.layout.charts_countries_cloud_view, container, false);

        activity = (MainActivity) getActivity();
        myDB = new DatabaseHelper(getContext());

        AnyChartView countriesCloudChart = rootView.findViewById(R.id.countries_cloud);
        new ChartHelper(myDB, getContext()).initCountriesCloudChart(countriesCloudChart);

        ImageView returnToStatsIcon = rootView.findViewById(R.id.return_to_stats_icon);
        returnToStatsIcon.setOnClickListener(this);

        return rootView;
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.return_to_stats_icon:
                activity.goToStatistics();
                break;
        }
    }

}
