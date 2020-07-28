# Travel app for Android
Android App that stores trips and shows interesting statistics of your travel history

## Getting started
The following files need to be added to `res/raw`:
- trips.csv
- citycontinents.csv
- worldcities.csv

An image named add_trip.png needs to be added to`res/mipmap`

Open project in Android Studio and run the app.

## Technologies used
- Java
- Android Studio
- SQLite

## To Do:
1. Export triplist worldcities, citycontinents as `*.csv`
1. Import csv files in app not R.raw (in settings)
1. Add a loading bar when reading csv files
1. Calculate DB statistics just once in TripStatisticsFragment and pass it on to Chart Fragments
1. Add table cityloc as menu item
1. Add "home" option in settings
1. Add TextWatcher to from/to City/Country - set long and lat to blank if text is changed
1. When returning to TripListView -> have year extended
1. Check in multi stop trip that dates are sequential
1. Final Refactoring
