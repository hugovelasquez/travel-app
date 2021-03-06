package com.sucaldo.travelapp.views;

import android.content.Context;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.core.content.res.ResourcesCompat;

import com.sucaldo.travelapp.R;
import com.sucaldo.travelapp.model.Trip;
import com.sucaldo.travelapp.model.YearListItem;

import java.util.List;
import java.util.Locale;

public class MultiColumnAdapterMyTrips extends ArrayAdapter<Object> {

    private LayoutInflater mInflater;
    // List of type Object because it can get inputs from two different classes Trip or Integer
    private List<Object> tripsAndYears;
    private int mViewResourceId;

    public MultiColumnAdapterMyTrips(Context context, int textViewResourceId, List<Object> tripsAndYears) {
        super(context, textViewResourceId, tripsAndYears);
        this.tripsAndYears = tripsAndYears;
        mInflater = (LayoutInflater) context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
        mViewResourceId = textViewResourceId;

        Log.d("TAG", "in MultiColumnAdapterMyTrips.class");
    }

    @NonNull
    public View getView(int position, View convertView, @NonNull ViewGroup parents) {
        convertView = mInflater.inflate(mViewResourceId, null);

        // Link to all textView IDs in list adapter view
        TextView startDate = convertView.findViewById(R.id.start_date);
        TextView endDate = convertView.findViewById(R.id.end_date);
        TextView fromCity = convertView.findViewById(R.id.from_city);
        TextView toCity = convertView.findViewById(R.id.to_city);
        TextView description = convertView.findViewById(R.id.description);
        ImageView dropdownArrow = convertView.findViewById(R.id.dropdown_arrow);

        Object tripOrYear = tripsAndYears.get(position);
        // Casting necessary to differentiate
        if (tripOrYear instanceof Trip) {
            Trip trip = (Trip) tripOrYear;
            startDate.setText(trip.getFormattedStartDate());
            endDate.setText(trip.getFormattedEndDate());
            fromCity.setText(trip.getFromCity());
            toCity.setText(trip.getToCity());
            description.setText(trip.getDescription());
            dropdownArrow.getLayoutParams().width = 0;
            dropdownArrow.requestLayout();
        }
        if (tripOrYear instanceof YearListItem) {
            YearListItem year = (YearListItem) tripOrYear;
            startDate.setText(String.format(Locale.getDefault(), "%d", year.getYear()));
            startDate.setTextColor(ResourcesCompat.getColor(convertView.getResources(), R.color.black, null));
            startDate.setTextAlignment(View.TEXT_ALIGNMENT_TEXT_START);

            if (year.isExpanded()) {
                dropdownArrow.setImageResource(R.drawable.ic_arrow_drop_up_black_24dp);
            } else {
                dropdownArrow.setImageResource(R.drawable.ic_arrow_drop_down_black_24dp);
            }

        }
        return convertView;
    }

}


