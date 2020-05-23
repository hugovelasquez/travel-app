package com.example.sucaldotravelapp;

import android.database.Cursor;
import android.os.Bundle;
import android.util.Log;
import android.widget.ListView;
import android.widget.Toast;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import java.util.ArrayList;

public class ViewTripInformation extends AppCompatActivity {

    // Definition of variables
    DatabaseHelper myDB;  // this is the DatabaseHelper class I created
    private ArrayList<User> userList; // Arraylist to store information from database. Contains elements of type User.
    User user; // this is the User class I created
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
        userList = new ArrayList<>();

        Log.d("TAG", "in ViewTripInformation.class");

        // Retrieve data from database. Call getListContents method from DatabaseHelper class.
        Cursor data = myDB.getListContents();
        // If not empty, add database info to userList
        int numRows = data.getCount();
        if (numRows == 0){
            Toast.makeText(ViewTripInformation.this, "Database is empty", Toast.LENGTH_LONG).show();
        } else {
            while(data.moveToNext()){
                // Definition of type User for userList. It contains info from all database columns.
//                user = new User(data.getString(1), data.getString(2), data.getString(3), data.getString(4),
//                        data.getString(5),data.getString(6),data.getString(7));
//                userList.add(user);

                Log.d("TAG", data.getString(1));
                Log.d("TAG", data.getString(6));
                Log.d("TAG", data.getString(7));
            }
            // Connect the userList (containing the desired database info) with my multi-column layout "list_adapter_view"
            listAdapter = new MultiColumnAdapter(this, R.layout.list_adapter_view, userList);
            // Adapt the one-column listView object to my multi-column layout
            listView.setAdapter(listAdapter);
        }
    }
}
