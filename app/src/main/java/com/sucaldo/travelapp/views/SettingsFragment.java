package com.sucaldo.travelapp.views;

import android.content.Context;
import android.os.AsyncTask;
import android.os.Bundle;
import android.os.Environment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.AutoCompleteTextView;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;

import com.sucaldo.travelapp.R;
import com.sucaldo.travelapp.db.CsvHelper;
import com.sucaldo.travelapp.db.DatabaseHelper;
import com.sucaldo.travelapp.model.AppPreferences;
import com.sucaldo.travelapp.model.CityLocation;

import java.io.File;
import java.util.List;

public class SettingsFragment extends Fragment implements View.OnClickListener {

    private DatabaseHelper myDB;
    private MainActivity activity;
    private CsvHelper csvHelper;
    private File exportPath;
    private AutoCompleteTextView homeCountry, homeCity;
    private AppPreferences appPreferences;

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        final View rootView = inflater.inflate(R.layout.settings_view, container, false);

        myDB = new DatabaseHelper(getContext());
        activity = (MainActivity) getActivity();
        csvHelper = new CsvHelper(myDB);
        appPreferences = new AppPreferences(getActivity().getApplicationContext(), myDB);

        homeCountry = rootView.findViewById(R.id.input_home_country);
        homeCity = rootView.findViewById(R.id.input_home_city);
        Button btnImportTrips = rootView.findViewById(R.id.btn_import_trips);
        Button btnDeleteTrips = rootView.findViewById(R.id.btn_delete_trips);
        Button btnExportAll = rootView.findViewById(R.id.btn_export_all);
        Button btnSaveHome = rootView.findViewById(R.id.btn_save_home);
        btnImportTrips.setOnClickListener(this);
        btnDeleteTrips.setOnClickListener(this);
        btnExportAll.setOnClickListener(this);
        btnSaveHome.setOnClickListener(this);

        exportPath = activity.getExternalFilesDir(Context.DOWNLOAD_SERVICE);
        TextView exportPathText = rootView.findViewById(R.id.export_path_text);
        exportPathText.setText(getString(R.string.export_path, exportPath));

        if (myDB.isCityLocTableEmpty()) {
            homeCountry.setError(getString(R.string.text_table_cityloc_empty));
            homeCity.setError(getString(R.string.text_table_cityloc_empty));
        } else {
            setDropdownOfCountries();
        }

        if (appPreferences.isHomeLocationPresent()) {
            CityLocation homeLocation = appPreferences.getSavedHomeLocation();
            homeCountry.setText(homeLocation.getCountry());
            homeCity.setText(homeLocation.getCity());
        }

        return rootView;
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.btn_import_trips:
                importTripsFromCsvFile();
                break;
            case R.id.btn_delete_trips:
                myDB.deleteAllTripsInDb();
                break;
            case R.id.btn_export_all:
                exportAllDataAsCsvFiles();
                break;
            case R.id.btn_save_home:
                storeHomeLocation();
                //homeCountry.setError(null);
                //homeCity.setError(null);
                break;
        }
    }

    private void importTripsFromCsvFile() {
        AsyncTask.execute(new Runnable() {
            @Override
            public void run() {
                csvHelper.readTripsCsvFile(getResources().openRawResource(R.raw.trips));
            }
        });
    }

    private void exportAllDataAsCsvFiles() {
        if (isExternalStorageAvailable() && !isExternalStorageReadOnly()) {
            AsyncTask.execute(new Runnable() {
                @Override
                public void run() {
                    csvHelper.writeTripsToCsv(exportPath);
                    csvHelper.writeCityLocationsToCsv(exportPath);
                }
            });
        } else {
            // TODO: what happens if wrong permissions?
            Toast.makeText(getContext(), "WRONG PERMISSIONS", Toast.LENGTH_LONG).show();
        }

    }

    private boolean isExternalStorageReadOnly() {
        String extStorageState = Environment.getExternalStorageState();
        if (Environment.MEDIA_MOUNTED_READ_ONLY.equals(extStorageState)) {
            return true;
        }
        return false;
    }

    private boolean isExternalStorageAvailable() {
        String extStorageState = Environment.getExternalStorageState();
        if (Environment.MEDIA_MOUNTED.equals(extStorageState)) {
            return true;
        }
        return false;
    }

    private void storeHomeLocation() {
        List<CityLocation> storedCityCoordinates = myDB.getStoredCityCoordinates(homeCountry.getText().toString(),
                homeCity.getText().toString());

        if (storedCityCoordinates.size() == 0) {
            homeCity.setError(getString(R.string.text_search_location_error));
        } else {
            appPreferences.storeCountrySelection(homeCountry.getText().toString());
            appPreferences.storeCitySelection(homeCity.getText().toString());
            Toast.makeText(getContext(), R.string.text_toast_home_saved, Toast.LENGTH_LONG).show();
        }
    }

    private void setDropdownOfCountries() {
        ArrayAdapter<String> adapter = new ArrayAdapter<>(getContext(),
                android.R.layout.simple_dropdown_item_1line, myDB.getCountries());
        homeCountry.setAdapter(adapter);
    }
}
