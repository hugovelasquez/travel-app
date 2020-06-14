package com.sucaldo.travelapp.db;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import android.util.Log;

import com.sucaldo.travelapp.model.Trip;

import java.util.ArrayList;
import java.util.List;

public class DatabaseHelper extends SQLiteOpenHelper {

    // Definition of variables
    public static final String DATABASE_NAME = "my_trips.db";
    public static final String TABLE_NAME = "trips";
    public static final String COL_ID = "ID";
    public static final String COL_GRP_ID = "GROUPID";
    public static final String COL_FRCTRY = "FROMCOUNTRY";
    public static final String COL_FRCTY = "FROMCITY";
    public static final String COL_TOCTRY = "TOCOUNTRY";
    public static final String COL_TOCTY = "TOCITY";
    public static final String COL_DESCR = "DESCRIPTION";
    public static final String COL_STDATE = "STARTDATE";
    public static final String COL_EDATE = "ENDDATE";

    // Initialization of Database
    public DatabaseHelper(Context context) {
        super(context, DATABASE_NAME, null, 1);
    }

    // Create table if Database is initialized
    @Override
    public void onCreate(SQLiteDatabase db) {
        // SQLite does not have data type varchar() or Date
        String createTable = "CREATE TABLE " + TABLE_NAME + " (ID INTEGER PRIMARY KEY AUTOINCREMENT, " +
                " FROMCOUNTRY TEXT, FROMCITY TEXT, TOCOUNTRY TEXT, TOCITY TEXT, DESCRIPTION TEXT, STARTDATE TEXT, ENDDATE TEXT, GROUPID INTEGER)";
        db.execSQL(createTable);
    }
    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
        db.execSQL("DROP IF TABLE EXISTS " + TABLE_NAME);
        onCreate(db);
    }

    private int getNextAvailableGroupId (){
        SQLiteDatabase db = this.getWritableDatabase();
        Cursor data = db.rawQuery("SELECT IFNULL(MAX(" + COL_GRP_ID + " ),0) FROM " + TABLE_NAME , null);

        while (data.moveToNext()) {
            int lastGroupId = Integer.valueOf(data.getString(0));
            return ++lastGroupId;
        }
        return 0;
    }


    // Method for adding a trip into database
    public Boolean addTrip(Trip trip) {
        SQLiteDatabase db = this.getWritableDatabase();
        ContentValues contentValues = new ContentValues();
        contentValues.put(COL_GRP_ID, getNextAvailableGroupId());
        contentValues.put(COL_FRCTRY, trip.getFromCountry());
        contentValues.put(COL_FRCTY, trip.getFromCity());
        contentValues.put(COL_TOCTRY, trip.getToCountry());
        contentValues.put(COL_TOCTY, trip.getToCity());
        contentValues.put(COL_DESCR, trip.getDescription());
        contentValues.put(COL_STDATE, trip.getStartDate().toString());
        contentValues.put(COL_EDATE, trip.getEndDate().toString());

        //Check if data has been allocated correctly. result shows a -1 if process did not work correctly.
        long result = db.insert(TABLE_NAME, null, contentValues);

        return result != -1;
    }

    public int getLastTripId (){
        SQLiteDatabase db = this.getWritableDatabase();
        Cursor data = db.rawQuery("SELECT last_insert_rowid()", null);

        while (data.moveToNext()) {
            return Integer.valueOf(data.getString(0));
        }
        return 0;
    }

    // Method for a query out of database
    public List<Trip> getAllTrips(){
        SQLiteDatabase db = this.getWritableDatabase();
        Cursor data = db.rawQuery("SELECT * FROM " + TABLE_NAME, null);

        int numRows = data.getCount();
        if (numRows == 0){
            // empty list will be returned
            return new ArrayList<>();
        } else {
            List<Trip> trips = new ArrayList<>();
            while (data.moveToNext()) {
               trips.add(new Trip(data));
            }
            return trips;
        }
    }

   // Method for retrieving trip out of database
    public Trip getTripById(int id){
        SQLiteDatabase db = this.getWritableDatabase();
        Cursor data = db.rawQuery("SELECT * FROM " + TABLE_NAME +
                " WHERE " + COL_ID + " = '" + id + "'", null);

        while (data.moveToNext()) {
            return new Trip(data);
        }
        return null;
    }

    // Method for updating a trip in the database
    public void updateTrip(Trip trip){
        SQLiteDatabase db = this.getWritableDatabase();
        db.execSQL("UPDATE " + TABLE_NAME + " SET " +
                COL_FRCTRY + " = '" + trip.getFromCountry() + "'," +
                COL_FRCTY + " = '" + trip.getFromCity() + "'," +
                COL_TOCTRY + " = '" + trip.getToCountry() + "'," +
                COL_TOCTY + " = '" + trip.getToCity() + "'," +
                COL_DESCR + " = '" + trip.getDescription() + "'," +
                COL_STDATE + " = '" + trip.getStartDate() + "'," +
                COL_EDATE + " = '" + trip.getEndDate() + "'," +
                COL_GRP_ID + " = '" + trip.getGroupId() + "'"+
                " WHERE " + COL_ID + " = " + trip.getId() );
    }

    // Method for deleting a field in database
    public void deleteTrip(int id){
        SQLiteDatabase db = this.getWritableDatabase();
        db.execSQL("DELETE FROM " + TABLE_NAME + " WHERE " + COL_ID + " = " + id);
    }
}
