package com.sucaldo.travelapp.views;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.AutoCompleteTextView;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ListView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;

import com.sucaldo.travelapp.R;
import com.sucaldo.travelapp.db.DatabaseHelper;
import com.sucaldo.travelapp.model.CityLocation;

import java.util.ArrayList;
import java.util.List;

public class CityCoordinatesFragment extends Fragment implements View.OnClickListener {

    private DatabaseHelper myDB;
    private ListView listView;
    private EditText searchCity;
    private AutoCompleteTextView searchCountry;

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View rootView = inflater.inflate(R.layout.city_coordinates_view, container, false);

        myDB = new DatabaseHelper(getContext());

        searchCountry = rootView.findViewById(R.id.search_country);
        searchCity = rootView.findViewById(R.id.search_city);
        listView = rootView.findViewById(R.id.listView_city_coordinates);
        Button btnSearch = rootView.findViewById(R.id.btn_search_coordinates);
        btnSearch.setOnClickListener(this);

        // Set dropdown of all countries
        ArrayAdapter<String> adapter = new ArrayAdapter<>(getContext(),
                android.R.layout.simple_dropdown_item_1line, myDB.getCountries());
        searchCountry.setAdapter(adapter);

        return rootView;
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.btn_search_coordinates:
                searchCountry.setError(null);
                searchCity.setError(null);
                searchCityCoordinates();
                break;
        }
    }

    private void searchCityCoordinates() {

        if (isSearchEntryEmpty()) {
            searchCountry.setError(getString(R.string.text_search_entry_error));
            searchCity.setError(getString(R.string.text_search_entry_error));
        } else {
            List<CityLocation> storedCityCoordinates = myDB.getStoredCityCoordinates(searchCountry.getText().toString(),
                    searchCity.getText().toString());
            // Multi column adapter requires a list of type Object
            if (storedCityCoordinates.size() == 0) {
                Toast.makeText(getContext(),getString(R.string.text_search_location_error),Toast.LENGTH_LONG).show();
            }
            List<Object> cityCoordinates = new ArrayList<Object>(storedCityCoordinates);
            setAdapterOfListView(cityCoordinates);
        }
    }

    private void setAdapterOfListView(List<Object> cityCoordinates) {
        MultiColumnAdapterCityCoordinates listAdapter = new MultiColumnAdapterCityCoordinates(getContext(),
                R.layout.list_adapter_city_coordinates_view, cityCoordinates);
        listView.setAdapter(listAdapter);
    }

    private boolean isSearchEntryEmpty() {
        boolean isSearchCountryEmpty = isFieldEmpty(searchCountry);
        boolean isSearchCityEmpty = isFieldEmpty(searchCity);
        // return true only if all are true
        return isSearchCountryEmpty && isSearchCityEmpty;
    }

    private boolean isFieldEmpty(EditText editText) {
        if (editText.getText().toString().isEmpty()) {
            return true;
        }
        return false;
    }
}
