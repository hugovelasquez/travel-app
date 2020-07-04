package com.sucaldo.travelapp.db;

import android.util.Log;

import com.opencsv.CSVReader;

import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;

public class CsvHelper {

    private final DatabaseHelper myDB;

    // Constructor to define input that CsvHelper receives
    public CsvHelper(DatabaseHelper myDB) {
        this.myDB = myDB;
    }

    public void readWorldCitiesCsvFile(InputStream inputStream) {
        try {
            CSVReader reader = new CSVReader(new InputStreamReader(inputStream));
            String[] nextLine;
            while ((nextLine = reader.readNext()) != null) {
                if(nextLine.length == 4) {
                    Log.d("CSV", nextLine[0] + nextLine[1] + nextLine[2] + nextLine[3]);
                    myDB.addCityLocItem(nextLine[3], nextLine[0], Float.parseFloat(nextLine[1]), Float.parseFloat(nextLine[2]));
                }
            }
        } catch (IOException e) {
            Log.e("CSV", "Line in .csv file could not be read");
        }
    }

    public void readCountryContinentCsvFile(InputStream inputStream) {
        try {
            CSVReader reader = new CSVReader(new InputStreamReader(inputStream));
            String[] nextLine;
            while ((nextLine = reader.readNext()) != null) {
                if(nextLine.length == 3) {
                    Log.d("CSV", nextLine[1] + nextLine[2]);
                    myDB.addCountryContinentItem(nextLine[2], nextLine[1]);
                }
            }
        } catch (IOException e) {
            Log.e("CSV", "Line in .csv file could not be read");
        }
    }
}
