package com.sucaldo.travelapp;

// I created this listAdapter class to help me define a bridge between the multi-column xml layout I created
// and the data I want to show. All rights go to YouTube Channel "CodingWithMitch".

import android.content.Context;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.TextView;
import java.util.ArrayList;

public class MultiColumnAdapter extends ArrayAdapter<User> {

    // Definition of variables
    private LayoutInflater mInflater;
    private ArrayList<User> users;
    private int mViewResourceId;


    public MultiColumnAdapter(Context context, int textViewResourceId, ArrayList<User> users) {
        super(context,textViewResourceId,users);
        this.users = users;
        mInflater = (LayoutInflater) context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
        mViewResourceId = textViewResourceId;

        Log.d("TAG", "in MultiColumnListAdapter.class");
    }

    public View getView(int position, View convertView, ViewGroup parents){
        convertView = mInflater.inflate(mViewResourceId,null);

        User user = users.get(position);

        if(user != null){
            // Reference to all textView Id's in the multi-column xml layout
            TextView startDateUser = (TextView) convertView.findViewById(R.id.txtlistStartDate);
            TextView endDateUser = (TextView) convertView.findViewById(R.id.txtlistEndDate);
            TextView fromCountryUser = (TextView) convertView.findViewById(R.id.txtlistFromCountry);
            TextView fromCityUser = (TextView) convertView.findViewById(R.id.txtlistFromCity);
            TextView toCountryUser = (TextView) convertView.findViewById(R.id.txtlistToCountry);
            TextView toCityUser = (TextView) convertView.findViewById(R.id.txtlistToCity);
            TextView tripDescUser = (TextView) convertView.findViewById(R.id.txtlistTripDesc);

//            if (startDateUser != null){
//                startDateUser.setText((user.getStartDateUser()));
//            }
//            if (endDateUser != null){
//                endDateUser.setText((user.getEndDateUser()));
//            }
//            if (fromCountryUser != null){
//                fromCountryUser.setText((user.getFromCountryUser()));
//            }
//            if (fromCityUser != null){
//                fromCityUser.setText((user.getFromCityUser()));
//            }
//            if (toCountryUser != null){
//                toCountryUser.setText((user.getToCountryUser()));
//            }
//            if (toCityUser != null){
//                toCityUser.setText((user.getToCityUser()));
//            }
//            if (tripDescUser != null){
//                tripDescUser.setText((user.getTripDescUser()));
//            }
        }
        return convertView;
    }
}
