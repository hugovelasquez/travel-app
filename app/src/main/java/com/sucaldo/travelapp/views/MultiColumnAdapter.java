package com.sucaldo.travelapp.views;

import android.content.Context;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.TextView;

import com.sucaldo.travelapp.R;
import com.sucaldo.travelapp.model.Trip;

import java.util.List;

public class MultiColumnAdapter extends ArrayAdapter<Object> {

    private LayoutInflater mInflater;
    // List of type Object because it can get inputs from two different classes Trip or Integer
    private List<Object> tripsAndYears;
    private int mViewResourceId;

    public MultiColumnAdapter(Context context, int textViewResourceId, List<Object> tripsAndYears) {
        super(context,textViewResourceId, tripsAndYears);
        this.tripsAndYears = tripsAndYears;
        mInflater = (LayoutInflater) context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
        mViewResourceId = textViewResourceId;

        Log.d("TAG", "in MultiColumnListAdapter.class");
    }

    public View getView(int position, View convertView, ViewGroup parents){
        convertView = mInflater.inflate(mViewResourceId,null);

        // Link to all textView IDs in list_adapter_view.xml
        TextView startDate = convertView.findViewById(R.id.start_date);
        TextView endDate = convertView.findViewById(R.id.end_date);
        TextView fromCity = convertView.findViewById(R.id.from_city);
        TextView toCity = convertView.findViewById(R.id.to_city);
        TextView description = convertView.findViewById(R.id.description);

        Object tripOrYear = tripsAndYears.get(position);
        // Casting necessary to differentiate
        if (tripOrYear instanceof Trip) {
            Trip trip = (Trip) tripOrYear;
            startDate.setText(trip.getFormattedStartDate());
            endDate.setText(trip.getFormattedEndDate());
            fromCity.setText(trip.getFromCity());
            toCity.setText(trip.getToCity());
            description.setText(trip.getDescription());
        }
        if (tripOrYear instanceof Integer){
            Integer year = (Integer) tripOrYear;
            startDate.setText(year.toString());
        }
        return convertView;
    }

}


