package com.sucaldo.travelapp.views;

import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ListView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;

import com.sucaldo.travelapp.R;
import com.sucaldo.travelapp.db.DatabaseHelper;
import com.sucaldo.travelapp.model.Trip;

import java.util.List;

public class MyTripsFragment extends Fragment {

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View rootView = inflater.inflate(R.layout.my_trips_view, container, false);

        ListView listView = rootView.findViewById(R.id.listView);

        DatabaseHelper myDB = new DatabaseHelper(getContext());
        List<Trip> trips = myDB.getAllTrips();
        // Adapt the one-column listView object to a multi-column layout
        MultiColumnAdapter listAdapter = new MultiColumnAdapter(getContext(), R.layout.list_adapter_view, trips);
        listView.setAdapter(listAdapter);

        // Define listener
        listView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                Trip trip = (Trip) parent.getItemAtPosition(position);
                // TODO open fragment and pass trip
            }
        });

        return rootView;
    }

}
