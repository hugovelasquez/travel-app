package com.sucaldo.travelapp.db;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import android.util.Log;

import com.sucaldo.travelapp.model.Trip;

public class DatabaseHelper extends SQLiteOpenHelper {

    // Definition of variables
    public static final String DATABASE_NAME = "my_trips.db";
    public static final String TABLE_NAME = "trips";
    public static final String COL_ID = "ID";
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
                " FROMCOUNTRY TEXT, FROMCITY TEXT, TOCOUNTRY TEXT, TOCITY TEXT, DESCRIPTION TEXT, STARTDATE TEXT, ENDDATE TEXT)";
        db.execSQL(createTable);
    }
    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
        db.execSQL("DROP IF TABLE EXISTS " + TABLE_NAME);
        onCreate(db);
    }

    // Method for adding a value into database
    public Boolean addTrip(Trip trip) {
        SQLiteDatabase db = this.getWritableDatabase();
        ContentValues contentValues = new ContentValues();
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

    // Method for a query out of the database
    public Cursor getListContents (){
        SQLiteDatabase db = this.getWritableDatabase();
        Cursor data = db.rawQuery("SELECT * FROM " + TABLE_NAME, null);
        return data;
    }

  /*  // Method for retrieving the ID of an specific list item
    public Cursor getItemID (String name){
        SQLiteDatabase db = this.getWritableDatabase();
        Cursor data = db.rawQuery("SELECT " + COL1 + " FROM " + TABLE_NAME +
                " WHERE " + COL2 + " = '" + name + "'", null);
        return data;
    }

    // Method for updating a field in database
    public void updateName(String newName, int id, String oldName){
        SQLiteDatabase db = this.getWritableDatabase();
        db.execSQL("UPDATE " + TABLE_NAME + " SET " + COL2 +
                " = '" + newName + "' WHERE " + COL1 + " = '" + id + "'" +
                " AND " + COL2 + " = '" + oldName + "'");
    }

    // Method for deleting a field in database
    public void deleteName(int id, String name){
        SQLiteDatabase db = this.getWritableDatabase();
        db.execSQL("DELETE FROM " + TABLE_NAME + " WHERE " + COL1 + " = '" + id + "'" +
                " AND " + COL2 + " = '" + name + "'");
    }*/
}
