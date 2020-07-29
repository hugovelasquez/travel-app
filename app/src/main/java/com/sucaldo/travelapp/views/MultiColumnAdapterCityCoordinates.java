package com.sucaldo.travelapp.views;

import android.content.Context;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.core.content.res.ResourcesCompat;

import com.sucaldo.travelapp.R;
import com.sucaldo.travelapp.model.CityLocation;
import com.sucaldo.travelapp.model.Trip;
import com.sucaldo.travelapp.model.YearListItem;

import java.util.List;
import java.util.Locale;

public class MultiColumnAdapterCityCoordinates extends ArrayAdapter<Object> {

    private LayoutInflater mInflater;
    private List<Object> cityCoordinates;
    private int mViewResourceId;

    public MultiColumnAdapterCityCoordinates(Context context, int textViewResourceId, List<Object> cityCoordinates) {
        super(context, textViewResourceId, cityCoordinates);
        this.cityCoordinates = cityCoordinates;
        mInflater = (LayoutInflater) context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
        mViewResourceId = textViewResourceId;

        Log.d("TAG", "in MultiColumnAdapterCityCoord.class");
    }

    @NonNull
    public View getView(int position, View convertView, @NonNull ViewGroup parents) {
        convertView = mInflater.inflate(mViewResourceId, null);

        TextView coordCountry = convertView.findViewById(R.id.coord_country);
        TextView coordCity = convertView.findViewById(R.id.coord_city);
        TextView coordLatitude = convertView.findViewById(R.id.coord_latitude);
        TextView coordLongitude = convertView.findViewById(R.id.coord_longitude);

        Object coordinates = cityCoordinates.get(position);
        CityLocation storedCoordinates = (CityLocation) coordinates;

        coordCountry.setText(storedCoordinates.getCountry());
        coordCity.setText(storedCoordinates.getCity());
        coordLatitude.setText(Float.toString(storedCoordinates.getLatitude()));
        coordLongitude.setText(Float.toString(storedCoordinates.getLongitude()));

        return convertView;
    }

}




