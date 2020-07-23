package com.sucaldo.travelapp.views.charts;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RadioButton;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;

import com.anychart.AnyChartView;
import com.anychart.charts.TagCloud;
import com.sucaldo.travelapp.R;
import com.sucaldo.travelapp.db.DatabaseHelper;
import com.sucaldo.travelapp.views.MainActivity;


public class CountriesCloudFragment extends Fragment implements View.OnClickListener {

    private MainActivity activity;
    private DatabaseHelper myDB;
    private AnyChartView countriesCloudChart;
    private TextView textView;
    private int countCountries, countPlaces;

    private TagCloud tagCloud;
    private ChartHelper chartHelper;

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        final View rootView = inflater.inflate(R.layout.charts_countries_cloud_view, container, false);

        activity = (MainActivity) getActivity();
        myDB = new DatabaseHelper(getContext());
        chartHelper = new ChartHelper(myDB, getContext());

        countCountries = myDB.getNumberOfVisitedCountries();
        countPlaces = myDB.getNumberOfVisitedPlaces();

        RadioButton radioCountriesCloud = rootView.findViewById(R.id.radio_countries_cloud);
        RadioButton radioCitiesCloud = rootView.findViewById(R.id.radio_cities_cloud);
        radioCountriesCloud.setOnClickListener(this);
        radioCitiesCloud.setOnClickListener(this);

        textView = rootView.findViewById(R.id.text_explanation_cloud_chart);
        countriesCloudChart = rootView.findViewById(R.id.countries_cloud);
        tagCloud = chartHelper.initCountriesCloudChart(countriesCloudChart, true);

        ImageView returnToStatsIcon = rootView.findViewById(R.id.return_to_stats_icon);
        returnToStatsIcon.setOnClickListener(this);

        // Default case is show countries cloud
        radioCountriesCloud.setChecked(true);
        setExplanationText(countCountries,R.string.text_visited_countries);

        return rootView;
    }

    @Override
    public void onClick(View v) {

        switch (v.getId()) {
            case R.id.return_to_stats_icon:
                activity.goToStatistics();
                break;
            case R.id.radio_countries_cloud:
                chartHelper.updateChart(tagCloud, true);
                setExplanationText(countCountries,R.string.text_visited_countries);
                break;
            case R.id.radio_cities_cloud:
                chartHelper.updateChart(tagCloud, false);
                setExplanationText(countPlaces,R.string.text_visited_places);
                break;
        }
    }

    private void setExplanationText(int count, int resource) {
        String explanationText = getString(resource, count);
        textView.setText(explanationText);
    }

}
