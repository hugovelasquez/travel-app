package com.sucaldo.travelapp.views;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ListView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentResultListener;

import com.sucaldo.travelapp.R;
import com.sucaldo.travelapp.db.DatabaseHelper;
import com.sucaldo.travelapp.model.Trip;
import com.sucaldo.travelapp.model.YearListItem;

import java.util.ArrayList;
import java.util.List;

public class MyTripsFragment extends Fragment {

    private MainActivity activity;
    private DatabaseHelper myDB;
    private ListView listView;

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View rootView = inflater.inflate(R.layout.my_trips_view, container, false);

        activity = (MainActivity) getActivity();
        myDB = new DatabaseHelper(getContext());

        // Set drawer item as selected - necessary because we uncheck it when leaving the fragment for trip details
        activity.navigationView.getMenu().getItem(0).setChecked(true);

        listView = rootView.findViewById(R.id.listView_my_trips);

        List<Integer> years = myDB.getAllYearsOfTrips();
        final List<YearListItem> yearItems = new ArrayList<>();
        for (int year : years) {
            yearItems.add(new YearListItem(year));
        }

        // List of type Object because it can get inputs from two different classes: Trip and Integer
        final List<Object> tripsAndYears = new ArrayList<Object>(yearItems);
        setAdapterOfListView(tripsAndYears);

        // The following lines will be automatically called only if there is a fragment request key from TripDetailsFragment.java.
        // This is the case when the user presses the cancel button and wants to go back to the extended trip view list.
        getParentFragmentManager().setFragmentResultListener(getString(R.string.fragment_request_key_year), this, new FragmentResultListener() {
            @Override
            public void onFragmentResult(@NonNull String key, @NonNull Bundle bundle) {
                int selectedYear = Integer.parseInt(bundle.getString(getString(R.string.fragment_key_year)));
                YearListItem yearItem = null;

                for (YearListItem yearListItem : yearItems) {
                    if (yearListItem.getYear().equals(selectedYear)) {
                        yearItem = yearListItem;
                    }
                }

                yearItem.setExpanded(true);
                addTripsOfClickedYearToList(yearItem, tripsAndYears);
            }
        });

        // Define listener for items in listView
        listView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                Object tripOrYear = parent.getItemAtPosition(position);
                if (tripOrYear instanceof Trip) {
                    // Casting necessary to differentiate
                    Trip trip = (Trip) tripOrYear;
                    openTripDetailFragment();
                    activity.passTripIdToOtherFragments(trip.getId(), getString(R.string.fragment_request_key_view));
                }
                if (tripOrYear instanceof YearListItem) {
                    // Casting necessary to differentiate
                    YearListItem yearItem = (YearListItem) tripOrYear;
                    if (yearItem.isExpanded()) {
                        yearItem.setExpanded(false);
                        removeTripsOfClickedYearFromList(yearItem, tripsAndYears);
                    } else {
                        yearItem.setExpanded(true);
                        addTripsOfClickedYearToList(yearItem, tripsAndYears);
                    }
                }

            }
        });

        return rootView;
    }

    private void openTripDetailFragment() {
        activity.getSupportFragmentManager().beginTransaction().replace(R.id.fragment_container,
                new TripDetailsFragment()).commit();
        activity.getSupportActionBar().setTitle(getString(R.string.navbar_trip_details));
        // unchecked drawer item because we are leaving this fragment
        activity.navigationView.getMenu().getItem(0).setChecked(false);
    }

    private void addTripsOfClickedYearToList(YearListItem yearItem, List<Object> tripsAndYears) {
        List<Trip> trips = myDB.getTripsOfYearSortedByDate(yearItem.getYear());
        // trips of clicked year to be added after that year in the list view
        tripsAndYears.addAll(tripsAndYears.indexOf(yearItem) + 1, trips);
        setAdapterOfListView(tripsAndYears);
    }

    private void removeTripsOfClickedYearFromList(YearListItem yearListItem, List<Object> tripsAndYears) {
        List<Object> tripsToRemove = new ArrayList<>();
        for (int i = (tripsAndYears.indexOf(yearListItem) + 1); i < tripsAndYears.size(); i++) {
            Object listItem = tripsAndYears.get(i);
            if (listItem instanceof Trip) {
                tripsToRemove.add(listItem);
            }
            if (listItem instanceof YearListItem) {
                break;
            }
        }
        tripsAndYears.removeAll(tripsToRemove);
        setAdapterOfListView(tripsAndYears);
    }

    private void setAdapterOfListView(List<Object> tripsAndYears) {
        MultiColumnAdapterMyTrips listAdapter = new MultiColumnAdapterMyTrips(getContext(), R.layout.list_adapter_my_trips_view, tripsAndYears);
        listView.setAdapter(listAdapter);
    }
}
