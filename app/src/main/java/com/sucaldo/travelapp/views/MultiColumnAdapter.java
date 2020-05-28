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

import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.List;

public class MultiColumnAdapter extends ArrayAdapter<Trip> {

    private LayoutInflater mInflater;
    private List<Trip> trips;
    private int mViewResourceId;

    public MultiColumnAdapter(Context context, int textViewResourceId, List<Trip> trips) {
        super(context,textViewResourceId, trips);
        this.trips = trips;
        mInflater = (LayoutInflater) context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
        mViewResourceId = textViewResourceId;

        Log.d("TAG", "in MultiColumnListAdapter.class");
    }

    public View getView(int position, View convertView, ViewGroup parents){
        convertView = mInflater.inflate(mViewResourceId,null);

        Trip trip = trips.get(position);

        if(trip != null){
            // Link to all textView IDs in list_adapter_view.xml
            TextView startDate = convertView.findViewById(R.id.start_date);
            TextView endDate = convertView.findViewById(R.id.end_date);
            TextView fromCity = convertView.findViewById(R.id.from_city);
            TextView toCity = convertView.findViewById(R.id.to_city);
            TextView description = convertView.findViewById(R.id.description);

            startDate.setText(getFormattedDate(trip.getStartDate()));
            endDate.setText(getFormattedDate(trip.getEndDate()));
            fromCity.setText(trip.getFromCity());
            toCity.setText(trip.getToCity());
            description.setText(trip.getDescription());
        }
        return convertView;
    }

    private String getFormattedDate (Date date){
        SimpleDateFormat dateFormat = new SimpleDateFormat(getContext().getString(R.string.date_format_pretty));
        return dateFormat.format(date);
    }
}


