package com.sucaldo.travelapp.views;

import android.app.AlertDialog;
import android.app.DatePickerDialog;
import android.content.DialogInterface;
import android.os.Bundle;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.ViewManager;
import android.widget.ArrayAdapter;
import android.widget.AutoCompleteTextView;
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
import com.sucaldo.travelapp.model.TripType;
import com.sucaldo.travelapp.model.CityLocation;
import com.sucaldo.travelapp.model.DistanceCalculator;
import com.sucaldo.travelapp.model.Trip;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;
import java.util.Locale;

public class AddTripFragment extends Fragment implements View.OnClickListener {

    // Definition of variables
    private EditText startDateField, endDateField;
    private TextInputEditText fromCity, toCity, description, fromLat, fromLong, toLat, toLong;
    private Date startDate, endDate;
    private MainActivity activity;
    private DatabaseHelper myDB;
    private RadioGroup radioGroup;
    private RadioButton radioReturn, radioOneWay, radioMultiStop;
    private TripType tripType;
    boolean editMode;
    private AutoCompleteTextView fromCountry, toCountry;

    private Trip trip;

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container,
                             @Nullable Bundle savedInstanceState) {
        // Link xml layout file with this fragment
        View rootView = inflater.inflate(R.layout.add_trip_view, container, false);

        activity = (MainActivity) getActivity();
        myDB = new DatabaseHelper(getContext());

        // Definition of variables
        startDateField = rootView.findViewById(R.id.trip_start_date);
        endDateField = rootView.findViewById(R.id.trip_end_date);
        ImageView startDateIcon = rootView.findViewById(R.id.start_date_icon);
        ImageView endDateIcon = rootView.findViewById(R.id.end_date_icon);
        fromCountry = rootView.findViewById(R.id.from_country);
        toCountry = rootView.findViewById(R.id.to_country);
        toCity = rootView.findViewById(R.id.to_city);
        fromCity = rootView.findViewById(R.id.from_city);
        description = rootView.findViewById(R.id.input_description);
        fromLat = rootView.findViewById(R.id.from_lat);
        fromLong = rootView.findViewById(R.id.from_long);
        toLat = rootView.findViewById(R.id.to_lat);
        toLong = rootView.findViewById(R.id.to_long);
        ImageView fromLocIcon = rootView.findViewById(R.id.from_loc_icon);
        ImageView toLocIcon = rootView.findViewById(R.id.to_loc_icon);
        Button btnSave = rootView.findViewById(R.id.btn_save);
        Button btnCancel = rootView.findViewById(R.id.btn_cancel);
        radioGroup = rootView.findViewById(R.id.radio_group);
        radioReturn = rootView.findViewById(R.id.radio_return);
        radioOneWay = rootView.findViewById(R.id.radio_one_way);
        radioMultiStop = rootView.findViewById(R.id.radio_multi);

        // Default case is return trip
        radioReturn.setChecked(true);
        tripType = TripType.RETURN;

        // Set dropdown of all countries
        ArrayAdapter<String> adapter = new ArrayAdapter<>(getContext(),
                android.R.layout.simple_dropdown_item_1line, myDB.getCountries());
        fromCountry.setAdapter(adapter);
        toCountry.setAdapter(adapter);

        // Define TextWatchers (--> set latitude and longitude automatically to blank in case user edits the location again)
        setTextWatcher(fromCountry, fromLat, fromLong);
        setTextWatcher(fromCity, fromLat, fromLong);
        setTextWatcher(toCountry, toLat, toLong);
        setTextWatcher(toCity, toLat, toLong);


        // This method will be automatically called only if trip fragment request key from TripDetailsFragment.java is present.
        // This is the case when selecting a trip in the trips view to edit its content.
        getParentFragmentManager().setFragmentResultListener(getString(R.string.fragment_request_key_edit), this, new FragmentResultListener() {
            @Override
            public void onFragmentResult(@NonNull String key, @NonNull Bundle bundle) {
                editMode = true;
                String tripIdString = bundle.getString(getString(R.string.fragment_key_trip_id));
                trip = myDB.getTripById(Integer.parseInt(tripIdString));
                tripType = trip.getType();

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
        radioReturn.setOnClickListener(this);
        radioOneWay.setOnClickListener(this);
        radioMultiStop.setOnClickListener(this);

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
                if (validateInputTextCharacters(fromCity)) {
                    getLocationOfCity(fromCountry.getText().toString(), fromCity.getText().toString(), fromLat, fromLong);
                }
                break;
            case R.id.to_loc_icon:
                if (validateInputTextCharacters(toCity)) {
                    getLocationOfCity(toCountry.getText().toString(), toCity.getText().toString(), toLat, toLong);
                }
                break;
            case R.id.radio_return:
                tripType = TripType.RETURN;
                break;
            case R.id.radio_one_way:
                tripType = TripType.ONE_WAY;
                endDateField.setError(null);
                break;
            case R.id.radio_multi:
                tripType = TripType.MULTI_STOP;
                endDateField.setError(null);
                break;
        }
    }

    private void setTextWatcher(EditText location, final EditText latitude, final EditText longitude) {
        location.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {
                latitude.setText("");
                longitude.setText("");
            }
            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
            }
            @Override
            public void afterTextChanged(Editable s) {
            }
        });
    }


    private void getLocationOfCity(String country, String city, TextInputEditText latitude, TextInputEditText longitude) {
        latitude.setError(null);
        longitude.setError(null);

        CityLocation cityLocation = myDB.getLocationOfCity(country, city);
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
            // no instantiating of DistanceCalculator necessary due to static method
            long distance = DistanceCalculator.getDistanceFromLatLongInKms(fromLatitude, fromLongitude, toLatitude, toLongitude);

            if (editMode) {
                updateTrip(fromCountryString, fromCityString, toCountryString, toCityString,
                        descriptionString, distance);
            } else {
                switch (tripType) {
                    case RETURN:
                        saveNewTrip(fromCountryString, fromCityString, toCountryString, toCityString,
                                descriptionString, -1, distance * 2, TripType.RETURN);
                        break;
                    case MULTI_STOP:
                        // See if it is the first stop of this multi-stop trip.
                        // If it is, the group id has to be set to -1 to indicate a new trip cluster
                        int groupId = trip == null ? -1 : trip.getGroupId();
                        saveNewTrip(fromCountryString, fromCityString, toCountryString, toCityString,
                                descriptionString, groupId, distance, TripType.MULTI_STOP);
                        break;
                    case ONE_WAY:
                        saveNewTrip(fromCountryString, fromCityString, toCountryString, toCityString,
                                descriptionString, -1, distance, TripType.ONE_WAY);
                        break;
                }
            }
        }
    }


    private void saveNewTrip(String fromCountryString, String fromCityString, String toCountryString,
                             String toCityString, String descriptionString, int groupId, long distance,
                             TripType type) {

        Trip newTrip = new Trip(fromCountryString, fromCityString, toCountryString, toCityString,
                descriptionString, startDate, endDate, groupId, distance,
                myDB.getContinentOfCountry(toCountryString), type);
        if (myDB.addTrip(newTrip)) {
            if (tripType.equals(TripType.RETURN) || tripType.equals(TripType.ONE_WAY)) {
                showSimpleTripSavedPopUpMessage();
            }
            if (tripType.equals(TripType.MULTI_STOP)) {
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
        trip.setToContinent(myDB.getContinentOfCountry(toCountryString));
        if (trip.getType().equals(TripType.RETURN)) {
            trip.setDistance(distance * 2);
        }
        if (trip.getType().equals(TripType.MULTI_STOP) || trip.getType().equals(TripType.ONE_WAY)) {
            trip.setDistance(distance);
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
                Trip trip = myDB.getTripById(myDB.getLastTripId());
                trip.setType(TripType.MULTI_STOP_LAST_STOP);
                trip.setToContinent(myDB.getContinentOfCountry(trip.getFromCountry()));
                myDB.updateTrip(trip);

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
        tripType = TripType.MULTI_STOP;
        radioGroup.setVisibility(View.INVISIBLE);
        trip = myDB.getTripById(myDB.getLastTripId());
        fromCountry.setText(trip.getToCountry());
        fromCity.setText(trip.getToCity());
        startDateField.setText(trip.getPickerFormattedEndDate());
        fromLat.setText(toLat.getText());
        fromLong.setText(toLong.getText());
        toCountry.setText(trip.getToCountry());

        startDate = null;
        endDate = null;

        toCity.setText("");
        description.setText("");
        endDateField.setText("");
        toLat.setText("");
        toLong.setText("");
        activity.getSupportActionBar().setTitle(getString(R.string.navbar_add_stop));
    }


    private boolean isTripValid() {
        boolean fromCountryValid = validateFieldNotEmpty(fromCountry);
        boolean fromCityValid = validateFieldNotEmpty(fromCity);
        boolean toCountryValid = validateFieldNotEmpty(toCountry);
        boolean toCityValid = validateFieldNotEmpty(toCity);
        boolean fromLatValid = validateFieldNotEmpty(fromLat);
        boolean toLatValid = validateFieldNotEmpty(toLat);
        boolean fromLongValid = validateFieldNotEmpty(fromLong);
        boolean toLongValid = validateFieldNotEmpty(toLong);
        boolean inputTextFromCityValid = validateInputTextCharacters(fromCity);
        boolean inputTextToCityValid = validateInputTextCharacters(toCity);
        boolean inputTextDescriptionValid = validateInputTextCharacters(description);
        boolean startAndEndDateValid = validateStartAndEndDate(startDateField, endDateField);
        // return true only if all are true
        return fromCountryValid && fromCityValid && toCountryValid && toCityValid && startAndEndDateValid
                && fromLatValid && fromLongValid && toLatValid && toLongValid && inputTextFromCityValid
                && inputTextToCityValid && inputTextDescriptionValid;
    }


    private boolean validateFieldNotEmpty(EditText editText) {
        if (editText.getText().toString().isEmpty()) {
            editText.setError(getString(R.string.text_empty_field_error));
            return false;
        }
        return true;
    }

    private boolean validateInputTextCharacters(EditText editText) {
        if (editText.getText().toString().contains(",") | editText.getText().toString().contains("'")) {
            editText.setError(getString(R.string.text_input_error));
            return false;
        }
        return true;
    }


    private boolean validateStartAndEndDate(EditText startDateField, EditText endDateField) {
        SimpleDateFormat formatter = new SimpleDateFormat(getString(R.string.date_format), Locale.getDefault());

        // start date cannot be empty
        // end date cannot be empty only if trip is of type RETURN
        if (!validateFieldNotEmpty(startDateField) |
                (tripType.equals(TripType.RETURN) && !validateFieldNotEmpty(endDateField))) {
            return false;
        }

        try {
            startDate = formatter.parse(startDateField.getText().toString());

        } catch (ParseException e) {
            startDateField.setError(getString(R.string.text_date_format_error));
            e.printStackTrace();
            return false;
        }
        if (!endDateField.getText().toString().equals("")) {
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
