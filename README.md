# Travel app for Android
Android App that stores trips and shows interesting statistics of your travel history

## Getting started
The following files need to be added to `res/raw`:
- trips.csv
- countriescontinents.csv
- worldcities.csv

An image named add_trip.png needs to be added to`res/mipmap`

Open project in Android Studio and run the app.

## Technologies used
- Java
- Android Studio
- SQLite

## To Do:
1. Import csv files in app (in settings), not via R.raw
1. Remove old csvReader methods
1. Add a loading bar when importing and exporting csv files (loading bar, success, and failure message)
1. Refactoring -> reorder columns in db
1. Calculate DB statistics just once in TripStatisticsFragment and pass it on to Chart Fragments
1. Add "home" option in settings
1. When returning to TripListView -> have year extended
1. Check in multi stop trip that dates are sequential
1. Rename package structure (get rid of .com.example.)
1. Final Refactoring
