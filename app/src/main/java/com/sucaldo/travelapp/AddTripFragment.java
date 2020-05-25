package com.sucaldo.travelapp;

import android.app.DatePickerDialog;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.ImageView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;

import java.util.Calendar;

public class AddTripFragment extends Fragment implements View.OnClickListener {

    // Definition of variables
    private DatePickerDialog picker;
    private EditText startDate, endDate;
    private ImageView startDateIcon, endDateIcon;

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        // Link xml layout file with this fragment
        View rootView = inflater.inflate(R.layout.add_trip_view, container, false);

        // Assign variables
        startDate = rootView.findViewById(R.id.trip_start_date);
        endDate = rootView.findViewById(R.id.trip_end_date);
        startDateIcon = rootView.findViewById(R.id.start_date_icon);
        endDateIcon = rootView.findViewById(R.id.end_date_icon);

        // Listener for start and end date
        startDateIcon.setOnClickListener(this);
        endDateIcon.setOnClickListener(this);

        return rootView;
    }

    @Override
    public void onClick(View v) {
        final Calendar cldr = Calendar.getInstance();
        int day = cldr.get(Calendar.DAY_OF_MONTH);
        int month = cldr.get(Calendar.MONTH);
        int year = cldr.get(Calendar.YEAR);

        switch (v.getId()) {
            case R.id.start_date_icon:
                picker = getPicker(year, month, day, startDate);
                picker.show();
                break;
            case R.id.end_date_icon:
                picker = getPicker(year, month, day, endDate);
                picker.show();
                break;
        }
    }

    private DatePickerDialog getPicker(int year, int month, int day, final EditText dateInput) {
        return new DatePickerDialog(getActivity(),
                new DatePickerDialog.OnDateSetListener() {
                    @Override
                    public void onDateSet(DatePicker view, int year, int monthOfYear, int dayOfMonth) {
                        dateInput.setText(dayOfMonth + "." + (monthOfYear + 1) + "." + year);
                    }
                }, year, month, day);

    }
}
