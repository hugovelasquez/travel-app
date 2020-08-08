package com.sucaldo.travelapp.db;

import android.content.Context;
import android.util.Log;
import android.widget.Toast;

import com.opencsv.CSVReader;
import com.sucaldo.travelapp.model.CityLocation;
import com.sucaldo.travelapp.model.DistanceCalculator;
import com.sucaldo.travelapp.model.Trip;
import com.sucaldo.travelapp.model.TripType;
import com.sucaldo.travelapp.views.MainActivity;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.util.List;

public class CsvHelper {

    private final DatabaseHelper myDB;
    private final String COLUMN_SEPARATOR = ",";
    private final String ROW_SEPARATOR = "\n";

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
                    myDB.addCityLocation(nextLine[3], nextLine[0], Float.parseFloat(nextLine[1]), Float.parseFloat(nextLine[2]));
                }
            }
        } catch (IOException e) {
            Log.e("CSV_CityLoc", "Line in .csv file could not be read");
        }
    }

    public void readCountriesContinentsCsvFile(InputStream inputStream) {
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
                            nextLine[6], nextLine[4], nextLine[5], TripType.valueOf(nextLine[8]));

                    String continent;

                    if(newTrip.getType().equals(TripType.MULTI_STOP_LAST_STOP)) {
                        continent = myDB.getContinentOfCountry(newTrip.getFromCountry());
                    } else {
                        continent = myDB.getContinentOfCountry(newTrip.getToCountry());
                    }
                    newTrip.setToContinent(continent);


                    CityLocation fromCityLoc = myDB.getLocationOfCity(newTrip.getFromCountry(), newTrip.getFromCity());
                    CityLocation toCityLoc = myDB.getLocationOfCity(newTrip.getToCountry(), newTrip.getToCity());

                    long distance = DistanceCalculator.getDistanceFromLatLongInKms(fromCityLoc.getLatitude(), fromCityLoc.getLongitude(),
                            toCityLoc.getLatitude(), toCityLoc.getLongitude());

                    if (newTrip.getType().equals(TripType.RETURN)) {
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


    public void writeTripsToCsv(File rootFile) {
        File csvFile = new File(rootFile, "trips.csv");

        String csvString = "";
        List<Trip> trips = myDB.getAllTrips();

        for (Trip trip : trips) {
            csvString += trip.getGroupId() + COLUMN_SEPARATOR
                    + trip.getFromCountry() + COLUMN_SEPARATOR
                    + trip.getFromCity() + COLUMN_SEPARATOR
                    + trip.getToCountry() + COLUMN_SEPARATOR
                    + trip.getToCity() + COLUMN_SEPARATOR
                    + trip.getDescription() + COLUMN_SEPARATOR
                    + trip.getStartDate() + COLUMN_SEPARATOR
                    + trip.getEndDateAsString() + COLUMN_SEPARATOR
                    + trip.getDistance() + COLUMN_SEPARATOR
                    + trip.getToContinent() + COLUMN_SEPARATOR
                    + trip.getType() + ROW_SEPARATOR;
        }
        writeCsvFile(csvFile, csvString);
    }

    public void writeCityLocationsToCsv(File rootFile) {
        File csvFile = new File(rootFile, "cityLocations.csv");

        String csvString = "";
        List<CityLocation> cityLocations = myDB.getAllCityLocations();

        for (CityLocation cityLocation : cityLocations) {
            csvString += cityLocation.getCountry() + COLUMN_SEPARATOR
                    + cityLocation.getCity() + COLUMN_SEPARATOR
                    + cityLocation.getLatitude() + COLUMN_SEPARATOR
                    + cityLocation.getLongitude() + ROW_SEPARATOR;
        }
        writeCsvFile(csvFile, csvString);
    }

    private void writeCsvFile(File file, String content) {
        try {
            FileOutputStream fos = new FileOutputStream(file);
            fos.write(content.getBytes());
            fos.close();
        } catch (IOException e) {
            // TODO error handling
            e.printStackTrace();
        }
    }


}
