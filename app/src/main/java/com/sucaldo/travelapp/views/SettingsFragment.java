package com.sucaldo.travelapp.views;

import android.content.Context;
import android.os.AsyncTask;
import android.os.Bundle;
import android.os.Environment;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.AutoCompleteTextView;
import android.widget.Button;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.ProgressBar;
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
    private CsvHelper csvHelper;
    private File exportPath;
    private AutoCompleteTextView homeCountry, homeCity;
    private AppPreferences appPreferences;
    private ProgressBar progressBar;
    private LinearLayout layoutProgressBar;

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        final View rootView = inflater.inflate(R.layout.settings_view, container, false);

        myDB = new DatabaseHelper(getContext());
        MainActivity activity = (MainActivity) getActivity();
        csvHelper = new CsvHelper(myDB);
        appPreferences = new AppPreferences(getActivity().getApplicationContext(), myDB);

        homeCountry = rootView.findViewById(R.id.input_home_country);
        homeCity = rootView.findViewById(R.id.input_home_city);
        progressBar = rootView.findViewById(R.id.progress_bar);
        layoutProgressBar = rootView.findViewById(R.id.layout_progress_bar);
        layoutProgressBar.setVisibility(View.INVISIBLE);

        Button btnImportTrips = rootView.findViewById(R.id.btn_import_trips);
        Button btnImportGeoData = rootView.findViewById(R.id.btn_import_geo_data);
        Button btnExportAll = rootView.findViewById(R.id.btn_export_all);
        Button btnSaveHome = rootView.findViewById(R.id.btn_save_home);
        btnImportTrips.setOnClickListener(this);
        btnImportGeoData.setOnClickListener(this);
        btnExportAll.setOnClickListener(this);
        btnSaveHome.setOnClickListener(this);

        exportPath = activity.getExternalFilesDir(Context.DOWNLOAD_SERVICE);
        TextView exportPathText = rootView.findViewById(R.id.export_path_text);
        exportPathText.setText(getString(R.string.export_path, exportPath));

        if (myDB.isCityLocTableEmpty()) {
            homeCountry.setError(getString(R.string.text_tables_empty_error));
        }

        setTextWatcher(homeCountry);

        // If home was already selected before, get that data and show it
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
            case R.id.btn_import_geo_data:
                importGeoDataFromCsvFiles();
                break;
            case R.id.btn_export_all:
                exportAllDataAsCsvFiles();
                break;
            case R.id.btn_save_home:
                storeHomeLocation();
                break;
        }
    }

    private void importTripsFromCsvFile() {
        if (myDB.isTripsTableEmpty()) {
            Toast.makeText(getContext(), getString(R.string.toast_trip_history_to_db), Toast.LENGTH_SHORT).show();
            // Due to low size of file no progress bar is necessary
            layoutProgressBar.setVisibility(View.INVISIBLE);

            AsyncTask.execute(new Runnable() {
                @Override
                public void run() {
                    csvHelper.readTripsCsvFile(getResources().openRawResource(R.raw.trips));
                }
            });
        } else {
            Toast.makeText(getContext(), getString(R.string.toast_trip_history_in_db), Toast.LENGTH_SHORT).show();
        }
    }

    private void exportAllDataAsCsvFiles() {
        Toast.makeText(getContext(), getString(R.string.toast_export_all_data), Toast.LENGTH_LONG).show();

        if (isExternalStorageAvailable() && !isExternalStorageReadOnly()) {
            AsyncTask.execute(new Runnable() {
                @Override
                public void run() {
                    csvHelper.writeTripsToCsv(exportPath);
                    csvHelper.writeCityLocationsToCsv(exportPath);
                    csvHelper.writeCountriesContinentsToCsv(exportPath);
                }
            });
        } else {
            // TODO: what happens if wrong permissions?
            Toast.makeText(getContext(), "WRONG PERMISSIONS", Toast.LENGTH_LONG).show();
        }

    }

    private boolean isExternalStorageReadOnly() {
        String extStorageState = Environment.getExternalStorageState();
        return Environment.MEDIA_MOUNTED_READ_ONLY.equals(extStorageState);
    }

    private boolean isExternalStorageAvailable() {
        String extStorageState = Environment.getExternalStorageState();
        return Environment.MEDIA_MOUNTED.equals(extStorageState);
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

    private void importGeoDataFromCsvFiles() {
        final boolean cityLocTableEmpty = myDB.isCityLocTableEmpty();
        final boolean countriesTableEmpty = myDB.isCountriesTableEmpty();
        if (cityLocTableEmpty || countriesTableEmpty) {
            Toast.makeText(getContext(), getString(R.string.toast_geographic_information_to_db), Toast.LENGTH_LONG).show();
            final ProgressBar pb = progressBar;
            layoutProgressBar.setVisibility(View.VISIBLE);

            Thread thread = new Thread(new Runnable() {
                @Override
                public void run() {
                    new CsvHelper(myDB).readCityLocationsCsvFile(getResources().openRawResource
                            (R.raw.city_locations), pb);

                    // Due to low size of file no real progress bar progression is necessary
                    new CsvHelper(myDB).readCountriesContinentsCsvFile(getResources().openRawResource
                            (R.raw.countries_continents));
                    pb.setProgress(0);
                }
            });
            thread.start();
        } else {
            Toast.makeText(getContext(), getString(R.string.toast_geographic_information_in_db), Toast.LENGTH_SHORT).show();
        }
    }

    private void setDropdownOfCountries() {
        ArrayAdapter<String> adapter = new ArrayAdapter<>(getContext(),
                android.R.layout.simple_dropdown_item_1line, myDB.getCountries());
        homeCountry.setAdapter(adapter);
    }

    // After importing geo data, TextWatcher is the only way to set the country dropdown menu without
    // having to leave the settings fragment to "refresh"
    private void setTextWatcher(EditText country) {
        country.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {
                setDropdownOfCountries();
            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
            }

            @Override
            public void afterTextChanged(Editable s) {
            }
        });
    }

}
