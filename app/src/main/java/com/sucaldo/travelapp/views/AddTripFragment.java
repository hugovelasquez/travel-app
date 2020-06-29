package com.sucaldo.travelapp.views;

import android.app.AlertDialog;
import android.app.DatePickerDialog;
import android.content.DialogInterface;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.ViewManager;
import android.widget.Button;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentResultListener;

import com.google.android.material.textfield.TextInputEditText;
import com.sucaldo.travelapp.R;
import com.sucaldo.travelapp.db.DatabaseHelper;
import com.sucaldo.travelapp.model.AddTripMode;
import com.sucaldo.travelapp.model.CityLocation;
import com.sucaldo.travelapp.model.Trip;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;
import java.util.Locale;

public class AddTripFragment extends Fragment implements View.OnClickListener {

    // Definition of variables
    private EditText startDateField, endDateField;
    private TextInputEditText fromCountry, fromCity, toCountry, toCity, description, fromLat, fromLong, toLat, toLong;
    private Date startDate, endDate;
    private MainActivity activity;
    private DatabaseHelper myDB;
    private RadioGroup radioGroup;
    private RadioButton radioSimple;
    private AddTripMode tripMode;

    private Trip trip;

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        // Link xml layout file with this fragment
        View rootView = inflater.inflate(R.layout.add_trip_view, container, false);

        activity = (MainActivity) getActivity();
        myDB = new DatabaseHelper(getContext());

        startDateField = rootView.findViewById(R.id.trip_start_date);
        endDateField = rootView.findViewById(R.id.trip_end_date);
        ImageView startDateIcon = rootView.findViewById(R.id.start_date_icon);
        ImageView endDateIcon = rootView.findViewById(R.id.end_date_icon);
        fromCountry = rootView.findViewById(R.id.from_country);
        toCountry = rootView.findViewById(R.id.to_country);
        toCity = rootView.findViewById(R.id.to_city);
        fromCity = rootView.findViewById(R.id.from_city);
        description = rootView.findViewById(R.id.input_descr);
        fromLat = rootView.findViewById(R.id.from_lat);
        fromLong = rootView.findViewById(R.id.from_long);
        toLat = rootView.findViewById(R.id.to_lat);
        toLong = rootView.findViewById(R.id.to_long);
        ImageView fromLocIcon = rootView.findViewById(R.id.from_loc_icon);
        ImageView toLocIcon = rootView.findViewById(R.id.to_loc_icon);
        Button btnSave = rootView.findViewById(R.id.btn_save);
        Button btnCancel = rootView.findViewById(R.id.btn_cancel);
        radioGroup = rootView.findViewById(R.id.radio_group);
        radioSimple = rootView.findViewById(R.id.radio_simple);

        // Default case is simple trip
        radioSimple.setChecked(true);
        tripMode = AddTripMode.ADD_SIMPLE_TRIP_MODE;

        // Method will be automatically called only if trip fragment request key from TripDetailsFragment.java is present
        // This is the case when selecting a trip in the trips view to edit its content.
        getParentFragmentManager().setFragmentResultListener(getString(R.string.fragment_request_key_edit), this, new FragmentResultListener() {
            @Override
            public void onFragmentResult(@NonNull String key, @NonNull Bundle bundle) {
                tripMode = AddTripMode.EDIT_MODE;
                String tripIdString = bundle.getString(getString(R.string.fragment_key_trip_id));
                trip = myDB.getTripById(Integer.parseInt(tripIdString));

                activity.getSupportActionBar().setTitle(getString(R.string.navbar_edit_trip));
                // RadioGroup not necessary
                ((ViewManager) radioGroup.getParent()).removeView(radioGroup);

                fromCountry.setText(trip.getFromCountry());
                fromCity.setText(trip.getFromCity());
                toCountry.setText(trip.getToCountry());
                toCity.setText(trip.getToCity());
                description.setText(trip.getDescription());
                startDateField.setText(trip.getPickerFormattedStartDate());
                endDateField.setText(trip.getPickerFormattedEndDate());
                getLocationOfCity(trip.getFromCountry(), trip.getFromCity(), fromLat, fromLong);
                getLocationOfCity(trip.getToCountry(), trip.getToCity(), toLat, toLong);
            }
        });

        // Set listeners
        startDateIcon.setOnClickListener(this);
        endDateIcon.setOnClickListener(this);
        btnSave.setOnClickListener(this);
        btnCancel.setOnClickListener(this);
        fromLocIcon.setOnClickListener(this);
        toLocIcon.setOnClickListener(this);

        return rootView;
    }

    @Override
    public void onClick(View v) {
        DatePickerDialog picker;
        switch (v.getId()) {
            case R.id.start_date_icon:
                picker = getPicker(startDateField);
                picker.show();
                break;
            case R.id.end_date_icon:
                picker = getPicker(endDateField);
                picker.show();
                break;
            case R.id.btn_save:
                saveTrip();
                break;
            case R.id.btn_cancel:
                activity.goToMyTrips();
                break;
            case R.id.from_loc_icon:
                getLocationOfCity(fromCountry.getText().toString(), fromCity.getText().toString(), fromLat, fromLong);
                break;
            case R.id.to_loc_icon:
                getLocationOfCity(toCountry.getText().toString(), toCity.getText().toString(), toLat, toLong);
                break;
        }
    }

    private void getLocationOfCity(String country, String city, TextInputEditText latitude, TextInputEditText longitude) {
        latitude.setError(null);
        longitude.setError(null);

        CityLocation cityLocation = myDB.getLatitudeAndLongitudeOfCity(country, city);
        if (cityLocation == null) {
            latitude.setError(getString(R.string.text_location_error));
            longitude.setError(getString(R.string.text_location_error));
        } else {
            latitude.setText(String.format(Locale.getDefault(), "%.4f", cityLocation.getLatitude()));
            longitude.setText(String.format(Locale.getDefault(), "%.4f", cityLocation.getLongitude()));
        }
    }

    private void saveTrip() {
        if (isTripValid()) {
            String fromCountryString = fromCountry.getText().toString();
            String fromCityString = fromCity.getText().toString();
            String toCountryString = toCountry.getText().toString();
            String toCityString = toCity.getText().toString();
            String descriptionString = description.getText().toString();
            float fromLatitude = Float.parseFloat(fromLat.getText().toString());
            float fromLongitude = Float.parseFloat(fromLong.getText().toString());
            float toLatitude = Float.parseFloat(toLat.getText().toString());
            float toLongitude = Float.parseFloat(toLong.getText().toString());
            // start and end Date already processed in method isTripValid

            myDB.saveCityLocationIfNotInDb(new CityLocation(fromCountryString, fromCityString, fromLatitude, fromLongitude));
            myDB.saveCityLocationIfNotInDb(new CityLocation(toCountryString, toCityString, toLatitude, toLongitude));
            long distance = getDistanceFromLatLongInKms(fromLatitude, fromLongitude, toLatitude, toLongitude);

            switch (tripMode) {
                case ADD_SIMPLE_TRIP_MODE:
                    saveNewTrip(fromCountryString, fromCityString, toCountryString, toCityString,
                            descriptionString, -1, radioSimple.isChecked() ? distance * 2 : distance);
                    break;
                case EDIT_MODE:
                    updateTrip(fromCountryString, fromCityString, toCountryString, toCityString,
                            descriptionString, distance);
                    break;
                case ADD_MULTI_TRIP_MODE:
                    saveNewTrip(fromCountryString, fromCityString, toCountryString, toCityString,
                            descriptionString, trip.getGroupId(), distance);
                    break;
            }
        }
    }

    private long getDistanceFromLatLongInKms(float lat1, float long1, float lat2, float long2) {
        int radiusEarth = 6371;
        double dLat = degToRadius(lat2 - lat1);
        double dLong = degToRadius(long2 - long1);
        double a =
                Math.sin(dLat / 2) * Math.sin(dLat / 2) +
                        Math.cos(degToRadius(lat1)) * Math.cos(degToRadius(lat2)) *
                                Math.sin(dLong / 2) * Math.sin(dLong / 2);
        double c = 2 * Math.atan2(Math.sqrt(a), Math.sqrt(1 - a));
        double d = radiusEarth * c;
        return Math.round(d);
    }

    private double degToRadius(float deg) {
        return deg * (Math.PI / 180);
    }

    private void saveNewTrip(String fromCountryString, String fromCityString, String toCountryString,
                             String toCityString, String descriptionString, int groupId, long distance) {
        Trip newTrip = new Trip(fromCountryString, fromCityString, toCountryString, toCityString,
                descriptionString, startDate, endDate, groupId, distance);
        if (myDB.addTrip(newTrip)) {
            if (radioSimple.isChecked()) {
                showSimpleTripSavedPopUpMessage();
            } else {
                showMultiTripSavedPopUpMessage();
            }
        } else {
            showTripSavedErrorPopUpMessage();
        }
    }


    private void updateTrip(String fromCountryString, String fromCityString, String toCountryString,
                            String toCityString, String descriptionString, long distance) {
        trip.setFromCountry(fromCountryString);
        trip.setFromCity(fromCityString);
        trip.setToCountry(toCountryString);
        trip.setToCity(toCityString);
        trip.setDescription(descriptionString);
        trip.setStartDate(startDate);
        trip.setEndDate(endDate);
        if (myDB.isTripMultiStop(trip.getGroupId())) {
            trip.setDistance(distance);
        } else {
            trip.setDistance(distance * 2);
        }

        myDB.updateTrip(trip);

        activity.goToMyTrips();
        Toast.makeText(getContext(), R.string.text_trip_updated, Toast.LENGTH_SHORT).show();
    }

    private void showSimpleTripSavedPopUpMessage() {
        AlertDialog alertDialog = new AlertDialog.Builder(getContext()).create();
        alertDialog.setCanceledOnTouchOutside(false);
        alertDialog.setTitle(getString(R.string.text_alert_dialog_trip_saved_title));
        alertDialog.setMessage(getString(R.string.text_alert_dialog_trip_saved_message));
        alertDialog.setButton(AlertDialog.BUTTON_POSITIVE, getString(R.string.text_go_to_trips), new DialogInterface.OnClickListener() {
            public void onClick(DialogInterface dialog, int id) {
                activity.goToMyTrips();
            }
        });

        alertDialog.setButton(AlertDialog.BUTTON_NEGATIVE, getString(R.string.text_add_another_trip), new DialogInterface.OnClickListener() {
            public void onClick(DialogInterface dialog, int id) {
                fromCountry.setText("");
                fromCity.setText("");
                toCountry.setText("");
                toCity.setText("");
                startDateField.setText("");
                endDateField.setText("");
                description.setText("");
                fromLat.setText("");
                fromLong.setText("");
                toLat.setText("");
                toLong.setText("");
            }
        });
        alertDialog.show();
    }

    private void showMultiTripSavedPopUpMessage() {
        AlertDialog alertDialog = new AlertDialog.Builder(getContext()).create();
        alertDialog.setCanceledOnTouchOutside(false);
        alertDialog.setTitle(getString(R.string.text_alert_dialog_stop_saved_title));
        alertDialog.setMessage(getString(R.string.text_alert_dialog_trip_saved_message));
        alertDialog.setButton(AlertDialog.BUTTON_POSITIVE, getString(R.string.text_add_next_stop), new DialogInterface.OnClickListener() {
            public void onClick(DialogInterface dialog, int id) {
                setupLayoutForNextStop();
            }
        });

        alertDialog.setButton(AlertDialog.BUTTON_NEGATIVE, getString(R.string.text_trip_completed), new DialogInterface.OnClickListener() {
            public void onClick(DialogInterface dialog, int id) {
                activity.goToMyTrips();
            }
        });
        alertDialog.show();
    }

    private void showTripSavedErrorPopUpMessage() {
        AlertDialog alertDialog = new AlertDialog.Builder(getContext()).create();
        alertDialog.setTitle(getString(R.string.text_alert_dialog_trip_not_saved_title));
        alertDialog.setMessage(getString(R.string.text_alert_dialog_trip_not_saved_message));
        alertDialog.show();
    }

    private void setupLayoutForNextStop() {
        tripMode = AddTripMode.ADD_MULTI_TRIP_MODE;
        radioGroup.setVisibility(View.INVISIBLE);
        trip = myDB.getTripById(myDB.getLastTripId());
        fromCountry.setText(trip.getToCountry());
        fromCity.setText(trip.getToCity());
        startDateField.setText(trip.getPickerFormattedEndDate());
        fromLat.setText(toLat.getText());
        fromLong.setText(toLong.getText());
        toCountry.setText(trip.getToCountry());

        toCity.setText("");
        description.setText("");
        endDateField.setText("");
        toLat.setText("");
        toLong.setText("");
        activity.getSupportActionBar().setTitle(getString(R.string.navbar_add_stop));
    }

    private boolean isTripValid() {
        boolean fromCountryValid = checkIfFieldInputIsEmpty(fromCountry);
        boolean fromCityValid = checkIfFieldInputIsEmpty(fromCity);
        boolean toCountryValid = checkIfFieldInputIsEmpty(toCountry);
        boolean toCityValid = checkIfFieldInputIsEmpty(toCity);
        boolean fromLatValid = checkIfFieldInputIsEmpty(fromLat);
        boolean toLatValid = checkIfFieldInputIsEmpty(toLat);
        boolean fromLongValid = checkIfFieldInputIsEmpty(fromLong);
        boolean toLongValid = checkIfFieldInputIsEmpty(toLong);
        boolean startAndEndDateValid = validateStartAndEndDate(startDateField, endDateField);
        // return true only if all are true
        return fromCountryValid && fromCityValid && toCountryValid && toCityValid && startAndEndDateValid
                && fromLatValid && fromLongValid && toLatValid && toLongValid;
    }

    private boolean checkIfFieldInputIsEmpty(EditText editText) {
        if (editText.getText().toString().isEmpty()) {
            editText.setError(getString(R.string.text_empty_field_error));
            return false;
        }
        return true;
    }

    private boolean validateStartAndEndDate(EditText startDateField, EditText endDateField) {
        SimpleDateFormat formatter = new SimpleDateFormat(getString(R.string.date_format), Locale.getDefault());

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
        final Calendar calendar = Calendar.getInstance();
        int day = calendar.get(Calendar.DAY_OF_MONTH);
        int month = calendar.get(Calendar.MONTH);
        int year = calendar.get(Calendar.YEAR);

        return new DatePickerDialog(getActivity(),
                new DatePickerDialog.OnDateSetListener() {
                    @Override
                    public void onDateSet(DatePicker view, int year, int monthOfYear, int dayOfMonth) {
                        dateInput.setText(getString(R.string.date_picker_format, dayOfMonth, monthOfYear + 1, year));
                        // if field not empty, remove error
                        dateInput.setError(null);
                    }
                }, year, month, day);

    }
}
