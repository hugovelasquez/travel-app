package com.sucaldo.travelapp.views;


import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.ViewManager;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentResultListener;

import com.sucaldo.travelapp.R;
import com.sucaldo.travelapp.db.DatabaseHelper;
import com.sucaldo.travelapp.model.Trip;
import com.sucaldo.travelapp.model.TripType;

import java.text.NumberFormat;
import java.util.List;
import java.util.Locale;


public class TripDetailsFragment extends Fragment implements View.OnClickListener {

    private MainActivity activity;
    private int tripId;
    private DatabaseHelper myDB;


    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        final View rootView = inflater.inflate(R.layout.trip_details_view, container, false);

        activity = (MainActivity) getActivity();
        Button btnEdit = rootView.findViewById(R.id.btn_edit_view);
        Button btnCancel = rootView.findViewById(R.id.btn_cancel_view);
        Button btnDelete = rootView.findViewById(R.id.btn_delete_view);

        btnCancel.setOnClickListener(this);
        btnDelete.setOnClickListener(this);
        btnEdit.setOnClickListener(this);

        myDB = new DatabaseHelper(getContext());

        // Get information from other fragment
        getParentFragmentManager().setFragmentResultListener(getString(R.string.fragment_request_key_view), this, new FragmentResultListener() {
            @Override
            public void onFragmentResult(@NonNull String key, @NonNull Bundle bundle) {
                String tripIdString = bundle.getString(getString(R.string.fragment_key_trip_id));
                tripId = Integer.parseInt(tripIdString);
                Trip trip = myDB.getTripById(tripId);

                TextView fromCountry = rootView.findViewById(R.id.from_country_view);
                TextView fromCity = rootView.findViewById(R.id.from_city_view);
                TextView toCountry = rootView.findViewById(R.id.to_country_view);
                TextView toCity = rootView.findViewById(R.id.to_city_view);
                TextView startDate = rootView.findViewById(R.id.trip_start_date_view);
                TextView endDate = rootView.findViewById(R.id.trip_end_date_view);
                TextView description = rootView.findViewById(R.id.trip_description_view);
                TextView distance = rootView.findViewById(R.id.trip_distance_view);
                TextView continent = rootView.findViewById(R.id.to_continent_view);
                TextView multiStopDescription = rootView.findViewById(R.id.trip_multi_trip_description_view);

                fromCountry.setText(trip.getFromCountry());
                fromCity.setText(trip.getFromCity());
                toCountry.setText(trip.getToCountry());
                toCity.setText(trip.getToCity());
                startDate.setText(trip.getFormattedStartDate());
                endDate.setText(trip.getFormattedEndDate());
                description.setText(trip.getDescription());
                distance.setText(getString(R.string.km_display_format, NumberFormat.getNumberInstance(Locale.GERMAN).format(trip.getDistance())));
                continent.setText(trip.getToContinent());

                // Add information of stop number and next stop if trip is part of a multi-stop-trip
                if (trip.getType().equals(TripType.MULTI_STOP)) {
                    List<Trip> multiStopTrips = myDB.getAllTripsOfMultiStopSortedByDate(trip.getGroupId());
                    int index = getMultiTripIndex(multiStopTrips, trip.getId());
                    String multiStopText = getString(R.string.multi_stop_number_text, index + 1);
                    if (index < multiStopTrips.size() - 1) {
                        String nextCity = multiStopTrips.get(index + 1).getToCity();
                        multiStopText += getString(R.string.multi_stop_next_stop_text, nextCity);
                    } else {
                        multiStopText += getString(R.string.multi_stop_final_stop_text);
                    }
                    multiStopDescription.setText(multiStopText);
                } else {
                    ((ViewManager) multiStopDescription.getParent()).removeView(multiStopDescription);
                }
            }
        });

        return rootView;
    }

    private int getMultiTripIndex(List<Trip> trips, int tripId) {
        for (Trip trip : trips) {
            if (trip.getId() == tripId) {
                return trips.indexOf(trip);
            }
        }
        return 0;
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.btn_cancel_view:
                activity.goToMyTrips();
                break;
            case R.id.btn_delete_view:
                myDB.deleteTrip(tripId);
                activity.goToMyTrips();
                Toast.makeText(getContext(), R.string.text_trip_deleted, Toast.LENGTH_SHORT).show();
                break;
            case R.id.btn_edit_view:
                activity.goToAddTrip();
                activity.passTripIdToOtherFragments(tripId, getString(R.string.fragment_request_key_edit));
                break;
        }
    }

}
