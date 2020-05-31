package com.sucaldo.travelapp.views;


import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentResultListener;

import com.sucaldo.travelapp.R;


public class TripDetailsFragment extends Fragment {

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View rootView = inflater.inflate(R.layout.trip_details_view, container, false);

        getParentFragmentManager().setFragmentResultListener(getString(R.string.fragment_key_request_key), this, new FragmentResultListener() {
            @Override
            public void onFragmentResult(@NonNull String key, @NonNull Bundle bundle) {
                String result = bundle.getString(getString(R.string.fragment_key_trip_id));
                //TODO get trip from db and display
            }
        });

        return rootView;
    }
}
