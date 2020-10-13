package com.sucaldo.travelapp.db;

import android.util.Log;
import android.widget.ProgressBar;

import com.opencsv.CSVReader;
import com.sucaldo.travelapp.model.CityLocation;
import com.sucaldo.travelapp.model.CountriesContinents;
import com.sucaldo.travelapp.model.Trip;
import com.sucaldo.travelapp.model.TripType;

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

    public void readCityLocationsCsvFile(InputStream inputStream, ProgressBar progressBar) {
        try {
            CSVReader reader = new CSVReader(new InputStreamReader(inputStream));
            int i = 0;
            int progressPercentage = 0;
            List<String[]> csvLines = reader.readAll();
            int IMPORT_TOTAL_STEPS = 40;
            int importStep = csvLines.size() / IMPORT_TOTAL_STEPS;
            for (String[] nextLine : csvLines) {
                // % is equivalent to MOD in Excel
                if (i++ % importStep == 0) {
                    progressPercentage += (100 / IMPORT_TOTAL_STEPS);
                    progressBar.setProgress(progressPercentage);
                }
                if (nextLine.length == 4) {
                    myDB.addCityLocation(nextLine[0], nextLine[1], Float.parseFloat(nextLine[2]),
                            Float.parseFloat(nextLine[3]));
                }
            }
            progressBar.setProgress(100);
        } catch (IOException e) {
            Log.e("CSV_CityLoc", "Line in .csv file could not be read");
        }
    }

    public void readCountriesContinentsCsvFile(InputStream inputStream) {
        try {
            CSVReader reader = new CSVReader(new InputStreamReader(inputStream));
            String[] nextLine;
            while ((nextLine = reader.readNext()) != null) {
                if(nextLine.length == 2) {
                    Log.d("CSV_CountryContinent", nextLine[0] + nextLine[1]);
                    myDB.addCountryContinentItem(nextLine[0], nextLine[1]);
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
                if(nextLine.length == 11) {
                    Log.d("CSV_trips", nextLine[3]);
                    Trip newTrip = new Trip(Integer.parseInt(nextLine[0]), nextLine[1], nextLine[2],
                            nextLine[3], nextLine[4], nextLine[5], nextLine[6], nextLine[7],
                            Integer.parseInt(nextLine[8]), nextLine[9], TripType.valueOf(nextLine[10]));
                    myDB.addTrip(newTrip);
                }
            }
        } catch (IOException e) {
            Log.e("CSV_trips", "Line in .csv file could not be read");
        }
    }


    public void writeTripsToCsv(File rootFile) {
        File csvFile = new File(rootFile, "trips.csv");

        StringBuilder csvString = new StringBuilder();
        List<Trip> trips = myDB.getAllTrips();

        for (Trip trip : trips) {
            csvString.append(trip.getGroupId())
                    .append(COLUMN_SEPARATOR)
                    .append(trip.getFromCountry())
                    .append(COLUMN_SEPARATOR)
                    .append(trip.getFromCity())
                    .append(COLUMN_SEPARATOR)
                    .append(trip.getToCountry())
                    .append(COLUMN_SEPARATOR)
                    .append(trip.getToCity())
                    .append(COLUMN_SEPARATOR)
                    .append(trip.getDescription())
                    .append(COLUMN_SEPARATOR)
                    .append(trip.getStartDate())
                    .append(COLUMN_SEPARATOR)
                    .append(trip.getEndDateAsString())
                    .append(COLUMN_SEPARATOR)
                    .append(trip.getDistance())
                    .append(COLUMN_SEPARATOR)
                    .append(trip.getToContinent())
                    .append(COLUMN_SEPARATOR)
                    .append(trip.getType())
                    .append(ROW_SEPARATOR);
        }
        writeCsvFile(csvFile, csvString.toString());
    }

    public void writeCityLocationsToCsv(File rootFile) {
        File csvFile = new File(rootFile, "city_locations.csv");

        StringBuilder csvString = new StringBuilder();
        List<CityLocation> cityLocations = myDB.getAllCityLocations();

        for (CityLocation cityLocation : cityLocations) {
            csvString.append(cityLocation.getCountry())
                    .append(COLUMN_SEPARATOR)
                    .append(cityLocation.getCity())
                    .append(COLUMN_SEPARATOR)
                    .append(cityLocation.getLatitude())
                    .append(COLUMN_SEPARATOR)
                    .append(cityLocation.getLongitude())
                    .append(ROW_SEPARATOR);
        }
        writeCsvFile(csvFile, csvString.toString());
    }

    public void writeCountriesContinentsToCsv(File rootFile) {
        File csvFile = new File(rootFile, "countries_continents.csv");

        StringBuilder csvString = new StringBuilder();
        List<CountriesContinents> countriesContinents = myDB.getAllCountriesContinents();

        for (CountriesContinents countriesContinent : countriesContinents) {
            csvString.append(countriesContinent.getContinent())
                    .append(COLUMN_SEPARATOR)
                    .append(countriesContinent.getCountry())
                    .append(ROW_SEPARATOR);
        }
        writeCsvFile(csvFile, csvString.toString());
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
