package com.sucaldo.travelapp.views.charts;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentResultListener;

import com.anychart.AnyChartView;
import com.sucaldo.travelapp.R;
import com.sucaldo.travelapp.db.DatabaseHelper;
import com.sucaldo.travelapp.model.ChartData;
import com.sucaldo.travelapp.views.MainActivity;


public class KmsAreaChartFragment extends Fragment implements View.OnClickListener {

    private MainActivity activity;

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        final View rootView = inflater.inflate(R.layout.charts_kms_area_chart_view, container, false);

        activity = (MainActivity) getActivity();
        DatabaseHelper myDB = new DatabaseHelper(getContext());
        final ChartHelper chartHelper = new ChartHelper(myDB, getContext());

        final AnyChartView kmsAreaChart = rootView.findViewById(R.id.kms_area_chart);

        ImageView returnToStatsIcon = rootView.findViewById(R.id.return_to_stats_icon);
        returnToStatsIcon.setOnClickListener(this);

        // Get data from TripStaticsFragment.java
        getParentFragmentManager().setFragmentResultListener(getString(R.string.fragment_request_key_area_chart), this, new FragmentResultListener() {
            @Override
            public void onFragmentResult(@NonNull String key, @NonNull Bundle bundle) {
                ChartData chartData = bundle.getParcelable(getString(R.string.fragment_key_area_chart));
                chartHelper.initKmsAreaChart(kmsAreaChart, true, chartData.getDataEntries());
            }
        });

        return rootView;
    }

    @Override
    public void onClick(View v) {
        if (v.getId() == R.id.return_to_stats_icon) {
            activity.goToStatistics();
        }
    }

}
