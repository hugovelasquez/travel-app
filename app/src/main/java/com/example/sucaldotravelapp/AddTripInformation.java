package com.example.sucaldotravelapp;

import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;


public class AddTripInformation extends AppCompatActivity {

    // Define variables
    private Button btnAddToList;
    DatabaseHelper myDB;  // this is the DatabaseHelper class I created
    private EditText fromCountry, fromCity, startDate, toCountry, toCity, endDate, tripDesc;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        // Link activity to layout xml
        setContentView(R.layout.trip_information_add);

        // Assign xml objects to variables
        btnAddToList = findViewById(R.id.btnAddToList);
        fromCountry = findViewById(R.id.editFromCountry);
        fromCity = findViewById(R.id.editFromCity);
        toCountry = findViewById(R.id.editToCountry);
        toCity = findViewById(R.id.editToCity);
        startDate = findViewById(R.id.editStartDate);
        endDate = findViewById(R.id.editEndDate);
        tripDesc = findViewById(R.id.editDescription);

        myDB = new DatabaseHelper(this);

        Log.d("TAG", "in AddTripInformation.class");

        // Click listener for btnAddToList
        btnAddToList.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                // Get text inserted by user
                String sDate = startDate.getText().toString();
                String eDate = endDate.getText().toString();
                String fCountry = fromCountry.getText().toString();
                String fCity = fromCity.getText().toString();
                String tCountry = toCountry.getText().toString();
                String tCity = toCity.getText().toString();
                String tDesc = tripDesc.getText().toString();

                // Add data only if fields are not blank. Call addData method from DatabaseHelper class.
                if (sDate.length() != 0 && eDate.length() !=0 && fCountry.length() != 0 && fCity.length() !=0
                && tCountry.length() != 0 && tCity.length() !=0 && tDesc.length() != 0) {
                    myDB.addData(sDate,eDate,fCountry,fCity,tCountry,tCity,tDesc);
                    // empty xml fields
                    startDate.setText("");
                    endDate.setText("");
                    fromCountry.setText("");
                    fromCity.setText("");
                    toCountry.setText("");
                    toCity.setText("");
                    tripDesc.setText("");

                    Toast.makeText(AddTripInformation.this, "Data has been saved", Toast.LENGTH_LONG).show();
                } else {
                    Toast.makeText(AddTripInformation.this, "One or more fields are still empty!", Toast.LENGTH_LONG).show();
                }

            }
        });


    }

}
