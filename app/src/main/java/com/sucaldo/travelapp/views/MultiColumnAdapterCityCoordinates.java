package com.sucaldo.travelapp.views;

import android.content.Context;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;

import com.sucaldo.travelapp.R;
import com.sucaldo.travelapp.db.DatabaseHelper;
import com.sucaldo.travelapp.model.CityLocation;
import com.sucaldo.travelapp.model.DistanceCalculator;
import com.sucaldo.travelapp.model.Trip;

import java.util.List;

public class MultiColumnAdapterCityCoordinates extends ArrayAdapter<Object> {

    private LayoutInflater mInflater;
    private List<Object> cityCoordinates;
    private int mViewResourceId;
    private DatabaseHelper myDB;

    public MultiColumnAdapterCityCoordinates(Context context, int textViewResourceId, List<Object> cityCoordinates) {
        super(context, textViewResourceId, cityCoordinates);
        this.cityCoordinates = cityCoordinates;
        mInflater = (LayoutInflater) context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
        mViewResourceId = textViewResourceId;
        myDB = new DatabaseHelper(context);

        Log.d("TAG", "in MultiColumnAdapterCityCoord.class");
    }

    @NonNull
    public View getView(int position, View convertView, @NonNull ViewGroup parents) {
        convertView = mInflater.inflate(mViewResourceId, null);

        TextView coordId = convertView.findViewById(R.id.coord_id);
        TextView coordCountry = convertView.findViewById(R.id.coord_country);
        final TextView coordCity = convertView.findViewById(R.id.coord_city);
        final TextView coordLatitude = convertView.findViewById(R.id.coord_latitude);
        final TextView coordLongitude = convertView.findViewById(R.id.coord_longitude);
        Button btnSaveCoord = convertView.findViewById(R.id.btn_save_coordinates);

        Object coordinates = cityCoordinates.get(position);
        final CityLocation storedCoordinates = (CityLocation) coordinates;

        coordId.setText(Integer.toString(storedCoordinates.getId()));
        coordCountry.setText(storedCoordinates.getCountry());
        coordCity.setText(storedCoordinates.getCity());
        coordLatitude.setText(Float.toString(storedCoordinates.getLatitude()));
        coordLongitude.setText(Float.toString(storedCoordinates.getLongitude()));

        btnSaveCoord.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                storedCoordinates.setLatitude(Float.parseFloat(coordLatitude.getText().toString()));
                storedCoordinates.setLongitude(Float.parseFloat(coordLongitude.getText().toString()));

                myDB.updateCityLocation(storedCoordinates);
                Toast.makeText(getContext(), R.string.text_toast_coord_saved, Toast.LENGTH_LONG).show();

                List<Trip> tripsToRecalculate = myDB.getTripsThatContainSpecificLocation(storedCoordinates.getCountry(),
                        storedCoordinates.getCity());
                updateTripsDistance(tripsToRecalculate);
            }
        });

        return convertView;
    }

    private void updateTripsDistance(List<Trip> trips) {
        for (Trip trip : trips) {
            CityLocation fromCityLoc = myDB.getLocationOfCity(trip.getFromCountry(), trip.getFromCity());
            CityLocation toCityLoc = myDB.getLocationOfCity(trip.getToCountry(), trip.getToCity());
            long distance = DistanceCalculator.getDistanceFromLatLongInKms(fromCityLoc.getLatitude(),
                    fromCityLoc.getLongitude(), toCityLoc.getLatitude(), toCityLoc.getLongitude());
            trip.setDistance(distance);
            myDB.updateTrip(trip);
        }
    }

}




