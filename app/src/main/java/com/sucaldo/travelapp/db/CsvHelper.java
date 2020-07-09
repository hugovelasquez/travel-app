package com.sucaldo.travelapp.db;

import android.util.Log;

import com.opencsv.CSVReader;
import com.sucaldo.travelapp.model.CityLocation;
import com.sucaldo.travelapp.model.DistanceCalculator;
import com.sucaldo.travelapp.model.Trip;

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
                    Log.d("CSV_CityLoc", nextLine[0] + nextLine[1] + nextLine[2] + nextLine[3]);
                    myDB.addCityLocItem(nextLine[3], nextLine[0], Float.parseFloat(nextLine[1]), Float.parseFloat(nextLine[2]));
                }
            }
        } catch (IOException e) {
            Log.e("CSV_CityLoc", "Line in .csv file could not be read");
        }
    }

    public void readCountryContinentCsvFile(InputStream inputStream) {
        try {
            CSVReader reader = new CSVReader(new InputStreamReader(inputStream));
            String[] nextLine;
            while ((nextLine = reader.readNext()) != null) {
                if(nextLine.length == 3) {
                    Log.d("CSV_CountryContinent", nextLine[1] + nextLine[2]);
                    myDB.addCountryContinentItem(nextLine[2], nextLine[1]);
                }
            }
        } catch (IOException e) {
            Log.e("CSV_CountryContinent", "Line in .csv file could not be read");
        }
    }

    public void readTripsCsvFile(InputStream inputStream) {
        try {
            CSVReader reader = new CSVReader(new InputStreamReader(inputStream));
            String[] nextLine;
            while ((nextLine = reader.readNext()) != null) {
                if(nextLine.length == 9) {
                    Log.d("CSV_trips", nextLine[3]);
                    Trip newTrip = new Trip(Integer.parseInt(nextLine[7]), nextLine[0], nextLine[1], nextLine[2], nextLine[3],
                            nextLine[6], nextLine[4], nextLine[5]);
                    boolean doubleDistance = nextLine[8].equals("1");
                    //Germany;Hamburg;Germany;Gottingen;25.10.2008;26.10.2008;Fiesta cumple Orbi con la WG;3;1
                    String continent = myDB.getContinentOfCountry(newTrip.getToCountry());
                    newTrip.setToContinent(continent);

                    CityLocation fromCityLoc = myDB.getLatitudeAndLongitudeOfCity(newTrip.getFromCountry(), newTrip.getFromCity());
                    CityLocation toCityLoc = myDB.getLatitudeAndLongitudeOfCity(newTrip.getToCountry(), newTrip.getToCity());

                    long distance = DistanceCalculator.getDistanceFromLatLongInKms(fromCityLoc.getLatitude(), fromCityLoc.getLongitude(),
                            toCityLoc.getLatitude(), toCityLoc.getLongitude());

                    if (doubleDistance){
                        distance = distance * 2;
                    }
                    newTrip.setDistance(distance);
                    myDB.addTrip(newTrip);
                }
            }
        } catch (IOException e) {
            Log.e("CSV_trips", "Line in .csv file could not be read");
        }
    }
}
