package com.sucaldo.travelapp.views;

import android.app.AlertDialog;
import android.app.DatePickerDialog;
import android.content.DialogInterface;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;

import com.google.android.material.textfield.TextInputEditText;
import com.sucaldo.travelapp.R;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;

public class AddTripFragment extends Fragment implements View.OnClickListener {

    // Definition of variables
    private DatePickerDialog picker;
    private EditText startDate, endDate;
    private ImageView startDateIcon, endDateIcon;
    private TextInputEditText fromCountry, fromCity, toCountry, toCity, description;
    private Button btnSave, btnCancel;

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
        fromCountry = rootView.findViewById(R.id.from_country);
        toCountry = rootView.findViewById(R.id.to_country);
        toCity = rootView.findViewById(R.id.to_city);
        fromCity = rootView.findViewById(R.id.from_city);
        description = rootView.findViewById(R.id.input_descr);
        btnSave = rootView.findViewById(R.id.btn_save);
        btnCancel = rootView.findViewById(R.id.btn_cancel);


        // Set listeners
        startDateIcon.setOnClickListener(this);
        endDateIcon.setOnClickListener(this);
        btnSave.setOnClickListener(this);
        btnCancel.setOnClickListener(this);

        return rootView;
    }

    @Override
    public void onClick(View v) {


        switch (v.getId()) {
            case R.id.start_date_icon:
                picker = getPicker(startDate);
                picker.show();
                break;
            case R.id.end_date_icon:
                picker = getPicker(endDate);
                picker.show();
                break;
            case R.id.btn_save:
                saveTrip();
                break;
            case R.id.btn_cancel:
                goToMyTrips();
                break;
        }
    }

    private void goToMyTrips() {
        MainActivity activity = (MainActivity) getActivity();
        activity.getSupportFragmentManager().beginTransaction().replace(R.id.fragment_container,
                new MyTripsFragment()).commit();
        activity.getSupportActionBar().setTitle(getString(R.string.navbar_my_trips));
        activity.navigationView.setCheckedItem(R.id.nav_my_trips);
    }

    private void saveTrip() {
        if (isTripValid()) {
            //TODO save trip to db
            showTripSavedPopUpMessage();
        }
    }

    private void showTripSavedPopUpMessage(){
        AlertDialog alertDialog = new AlertDialog.Builder(getContext()).create();
        alertDialog.setCanceledOnTouchOutside(false);
        alertDialog.setTitle("Trip Saved");
        alertDialog.setMessage("What do you want to do?");
        alertDialog.setButton(AlertDialog.BUTTON_POSITIVE, "Go to My Trips", new DialogInterface.OnClickListener() {
            public void onClick(DialogInterface dialog, int id) {
                goToMyTrips();
            } });

        alertDialog.setButton(AlertDialog.BUTTON_NEGATIVE, "Add another Trip", new DialogInterface.OnClickListener() {
            public void onClick(DialogInterface dialog, int id) {
                fromCountry.setText("");
                fromCity.setText("");
                toCountry.setText("");
                toCity.setText("");
                startDate.setText("");
                endDate.setText("");
                description.setText("");
            }});
        alertDialog.show();
    }

    private boolean isTripValid() {
        boolean fromCountryValid = checkIfFieldInputIsEmpty(fromCountry);
        boolean fromCityValid = checkIfFieldInputIsEmpty(fromCity);
        boolean toCountryValid = checkIfFieldInputIsEmpty(toCountry);
        boolean toCityValid = checkIfFieldInputIsEmpty(toCity);
        boolean startAndEndDateValid = validateStartAndEndDate(startDate, endDate);
        // return true only if all are true
        return fromCountryValid && fromCityValid && toCountryValid && toCityValid && startAndEndDateValid;
    }

    private boolean checkIfFieldInputIsEmpty(EditText editText) {
        if (editText.getText().toString().isEmpty()) {
            editText.setError(getString(R.string.text_empty_field_error));
            return false;
        }
        return true;
    }

    private boolean validateStartAndEndDate(EditText startDateField, EditText endDateField) {
        SimpleDateFormat formatter = new SimpleDateFormat("d.M.yyyy");
        Date startDate;
        Date endDate;

        // Return false if either start or end Date field are empty
        if (!checkIfFieldInputIsEmpty(startDateField) | !checkIfFieldInputIsEmpty(endDateField)) {
            return false;
        }

        try {
            startDate = formatter.parse(startDateField.getText().toString());

        } catch (ParseException e) {
            startDateField.setError(getString(R.string.text_date_format_error));
            e.printStackTrace();
            return false;
        }
        try {
            endDate = formatter.parse(endDateField.getText().toString());

        } catch (ParseException e) {
            endDateField.setError(getString(R.string.text_date_format_error));
            e.printStackTrace();
            return false;
        }
        if (startDate.after(endDate)) {
            endDateField.setError(getString(R.string.text_end_date_error));
            return false;
        }
        return true;
    }

    private DatePickerDialog getPicker(final EditText dateInput) {
        final Calendar cldr = Calendar.getInstance();
        int day = cldr.get(Calendar.DAY_OF_MONTH);
        int month = cldr.get(Calendar.MONTH);
        int year = cldr.get(Calendar.YEAR);

        return new DatePickerDialog(getActivity(),
                new DatePickerDialog.OnDateSetListener() {
                    @Override
                    public void onDateSet(DatePicker view, int year, int monthOfYear, int dayOfMonth) {
                        dateInput.setText(dayOfMonth + "." + (monthOfYear + 1) + "." + year);
                        // if field not empty, remove error
                        dateInput.setError(null);
                    }
                }, year, month, day);

    }
}
