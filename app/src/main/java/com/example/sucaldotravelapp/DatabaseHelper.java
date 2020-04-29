package com.example.sucaldotravelapp;

// I have created this new class called DatabaseHelper to help me set up a database for my App.
// It works using SQLite. All rights go to YouTube Channel "CodingWithMitch".


import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

public class DatabaseHelper extends SQLiteOpenHelper {

    // Definition of variables
    public static final String DATABASE_NAME = "my_trips.db";
    public static final String TABLE_NAME = "my_trips_data";
    public static final String COL1 = "ID";
    public static final String COL2 = "STARTDATE";
    public static final String COL3 = "ENDDATE";
    public static final String COL4 = "FROMCOUNTRY";
    public static final String COL5 = "FROMCITY";
    public static final String COL6 = "TOCOUNTRY";
    public static final String COL7 = "TOCITY";
    public static final String COL8 = "TRIPDESC";

    // Initialization of Database
    public DatabaseHelper(Context context) {
        super(context, DATABASE_NAME, null, 1);
    }

    // Creation of table
    @Override
    public void onCreate(SQLiteDatabase db) {
        String createTable = "CREATE TABLE " + TABLE_NAME + " (ID INTEGER PRIMARY KEY AUTOINCREMENT, " +
                " STARTDATE TEXT, ENDDATE TEXT, FROMCOUNTRY TEXT, FROMCITY TEXT, TOCOUNTRY TEXT, TOCITY TEXT, TRIPDESC TEXT)";
        db.execSQL(createTable);
    }
    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
        db.execSQL("DROP IF TABLE EXISTS " + TABLE_NAME);
        onCreate(db);
    }

    // Method for adding a value into database
    public Boolean addData(String sDate, String eDate, String fCountry, String fCity, String tCountry, String tCity, String tDesc) {
        SQLiteDatabase db = this.getWritableDatabase();
        ContentValues contentValues = new ContentValues();
        contentValues.put(COL2, sDate);
        contentValues.put(COL3, eDate);
        contentValues.put(COL4, fCountry);
        contentValues.put(COL5, fCity);
        contentValues.put(COL6, tCountry);
        contentValues.put(COL7, tCity);
        contentValues.put(COL8, tDesc);

        //Check if data has been allocated correctly. result shows a -1 if process did not work correctly.
        long result = db.insert(TABLE_NAME, null, contentValues);

        if (result == -1) {
            return false;
        } else {
            return true;
        }
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
