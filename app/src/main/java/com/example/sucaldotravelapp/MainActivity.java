package com.example.sucaldotravelapp;

import androidx.appcompat.app.AppCompatActivity;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;


public class MainActivity extends AppCompatActivity {

    // Define variables
    private Button btnAdd, btnView;
    DatabaseHelper myDB;  // this is the DatabaseHelper class I created

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        // Link activity to layout xml
        setContentView(R.layout.activity_main);

        // Assignment of xml objects to variables
        btnAdd = findViewById(R.id.btnAdd);
        btnView = findViewById(R.id.btnView);

        myDB = new DatabaseHelper(this);

        // Click listener for btnAdd. It links this activity with the new activity "AddTripInformation".
        btnAdd.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent addTripInfoIntent = new Intent(MainActivity.this, AddTripInformation.class);
                startActivity(addTripInfoIntent);
            }
        });

        // Click listener for btnView. It links this activity with the new activity "ViewTripInformation".
        btnView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent viewTripInfoIntent = new Intent(MainActivity.this, ViewTripInformation.class);
                startActivity(viewTripInfoIntent);
            }
        });
    }
}
