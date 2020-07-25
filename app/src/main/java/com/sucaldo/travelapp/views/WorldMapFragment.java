package com.sucaldo.travelapp.views;


import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;

import com.sucaldo.travelapp.R;
import com.sucaldo.travelapp.db.DatabaseHelper;

public class WorldMapFragment extends Fragment implements View.OnClickListener {

    private final int WORLD_CIRCUMFERENCE = 40075;

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        final View rootView = inflater.inflate(R.layout.world_map_view, container, false);

        DatabaseHelper myDB = new DatabaseHelper(getContext());
        int totalKms = myDB.getTotalKms();
        float timesAroundWorld = (float) totalKms/WORLD_CIRCUMFERENCE;

        TextView timesAroundWorldText = rootView.findViewById(R.id.world_map_travel);
        timesAroundWorldText.setText(getString(R.string.text_world_map_travel, timesAroundWorld));

        return rootView;
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {

        }
    }

}
