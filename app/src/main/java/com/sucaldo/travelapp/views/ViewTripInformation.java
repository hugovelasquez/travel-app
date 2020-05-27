/*
package com.sucaldo.travelapp.views;

import android.database.Cursor;
import android.os.Bundle;
import android.util.Log;
import android.widget.ListView;
import android.widget.Toast;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;

import com.sucaldo.travelapp.R;
import com.sucaldo.travelapp.db.DatabaseHelper;
import com.sucaldo.travelapp.model.Trip;

import java.util.ArrayList;
import java.util.List;

public class ViewTripInformation extends AppCompatActivity {

    // Definition of variables
    DatabaseHelper myDB;  // this is the DatabaseHelper class I created
    private List<Trip> tripList; // Arraylist to store information from database. Contains elements of type User.
    Trip trip; // this is the User class I created
    MultiColumnAdapter listAdapter; // this is the MultiColumnListAdapter class I created
    private ListView listView;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        // link this activity to layout xml
        setContentView(R.layout.my_trips_view);

        // Assignment of xml object
        listView = findViewById(R.id.listView);

        myDB = new DatabaseHelper(this);

        Log.d("TAG", "in ViewTripInformation.class");

        // Retrieve data from database. Call getListContents method from DatabaseHelper class.
        tripList = myDB.getAllTrips();


//            // Connect the userList (containing the desired database info) with my multi-column layout "list_adapter_view"
//            listAdapter = new MultiColumnAdapter(this, R.layout.list_adapter_view, tripList);
//            // Adapt the one-column listView object to my multi-column layout
//            listView.setAdapter(listAdapter);
        }
    }
}
*/
