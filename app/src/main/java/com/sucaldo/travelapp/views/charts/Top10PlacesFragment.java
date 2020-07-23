package com.sucaldo.travelapp.views.charts;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;

import com.anychart.AnyChartView;
import com.sucaldo.travelapp.R;
import com.sucaldo.travelapp.db.DatabaseHelper;
import com.sucaldo.travelapp.views.MainActivity;


public class Top10PlacesFragment extends Fragment implements View.OnClickListener {

    private MainActivity activity;
    private DatabaseHelper myDB;

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        final View rootView = inflater.inflate(R.layout.charts_top_10_places_view, container, false);

        activity = (MainActivity) getActivity();
        myDB = new DatabaseHelper(getContext());

        AnyChartView top10CitiesChart = rootView.findViewById(R.id.top_10_places);
        new ChartHelper(myDB, getContext()).initTop10PlacesChart(top10CitiesChart, true);

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
