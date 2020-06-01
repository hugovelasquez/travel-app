package com.sucaldo.travelapp.views;


import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentResultListener;

import com.sucaldo.travelapp.R;
import com.sucaldo.travelapp.db.DatabaseHelper;
import com.sucaldo.travelapp.model.Trip;


public class TripDetailsFragment extends Fragment {

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        final View rootView = inflater.inflate(R.layout.trip_details_view, container, false);

        getParentFragmentManager().setFragmentResultListener(getString(R.string.fragment_key_request_key), this, new FragmentResultListener() {
            @Override
            public void onFragmentResult(@NonNull String key, @NonNull Bundle bundle) {
                String tripId = bundle.getString(getString(R.string.fragment_key_trip_id));

                DatabaseHelper myDB = new DatabaseHelper(getContext());
                Trip trip = myDB.getTripById(tripId);

                TextView fromCountry = rootView.findViewById(R.id.from_country_view);
                TextView fromCity = rootView.findViewById(R.id.from_city_view);
                TextView toCountry = rootView.findViewById(R.id.to_country_view);
                TextView toCity = rootView.findViewById(R.id.to_city_view);
                TextView startDate = rootView.findViewById(R.id.trip_start_date_view);
                TextView endDate = rootView.findViewById(R.id.trip_end_date_view);
                TextView description = rootView.findViewById(R.id.trip_description_view);

                fromCountry.setText(trip.getFromCountry());
                fromCity.setText(trip.getFromCity());
                toCountry.setText(trip.getToCountry());
                toCity.setText(trip.getToCity());
                startDate.setText(trip.getFormattedStartDate());
                endDate.setText(trip.getFormattedEndDate());
                description.setText(trip.getDescription());
            }
        });

        return rootView;
    }
}
