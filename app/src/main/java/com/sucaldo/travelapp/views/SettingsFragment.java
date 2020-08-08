package com.sucaldo.travelapp.views;

import android.content.Context;
import android.os.AsyncTask;
import android.os.Bundle;
import android.os.Environment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;

import com.sucaldo.travelapp.R;
import com.sucaldo.travelapp.db.CsvHelper;
import com.sucaldo.travelapp.db.DatabaseHelper;

import java.io.File;

public class SettingsFragment extends Fragment implements View.OnClickListener {

    private DatabaseHelper myDB;
    private MainActivity activity;
    private CsvHelper csvHelper;
    private File exportPath;

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        final View rootView = inflater.inflate(R.layout.settings_view, container, false);

        myDB = new DatabaseHelper(getContext());
        activity = (MainActivity) getActivity();
        csvHelper = new CsvHelper(myDB);

        Button btnImportTrips = rootView.findViewById(R.id.btn_import_trips);
        Button btnDeleteTrips = rootView.findViewById(R.id.btn_delete_trips);
        Button btnExportAll = rootView.findViewById(R.id.btn_export_all);
        btnImportTrips.setOnClickListener(this);
        btnDeleteTrips.setOnClickListener(this);
        btnExportAll.setOnClickListener(this);

        exportPath = activity.getExternalFilesDir(Context.DOWNLOAD_SERVICE);
        TextView exportPathText = rootView.findViewById(R.id.export_path_text);
        exportPathText.setText(getString(R.string.export_path, exportPath));

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
}
