package com.sucaldo.travelapp.views;

import android.os.AsyncTask;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;

import com.sucaldo.travelapp.R;
import com.sucaldo.travelapp.db.CsvHelper;
import com.sucaldo.travelapp.db.DatabaseHelper;

public class SettingsFragment extends Fragment implements View.OnClickListener {
    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        final View rootView = inflater.inflate(R.layout.settings_view, container, false);

        Button btnImportTrips = rootView.findViewById(R.id.btn_import_trips);
        btnImportTrips.setOnClickListener(this);

        return rootView;
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.btn_import_trips:
                importTripsFromCsvFile();
                break;
        }
    }

    private void importTripsFromCsvFile() {
        final DatabaseHelper myDB = new DatabaseHelper(getContext());
        AsyncTask.execute(new Runnable() {
            @Override
            public void run() {
                new CsvHelper(myDB).readTripsCsvFile(getResources().openRawResource(R.raw.trips));
            }
        });
    }
}
