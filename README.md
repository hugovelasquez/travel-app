# Travel app for Android
Android App that stores trips and shows interesting statistics of your travel history

# Getting started
The following files need to be added to `res/raw`:
- trips.csv
- citycontinents.csv
- worldcities.csv

An image named add_trip.png needs to be added to`res/mipmap`

Open project in Android Studio and run the app.

# Technologies used
- Java
- Android Studio
- SQLite

# To Do:
1. Tag cloud - All visited countries
1. Area chart - Traveled kms per continent (Last trip of multi-stop trip: only kms count towards statistics)
1. Bubble chart - Trips and kms per year (Last trip of multi-stop trip: only kms count towards statistics)
1. World Map
1. World view - Times traveled around the world
1. Refactoring
1. Export triplist worldcities, citycontinents as `*.csv`
1. Import csv files in app not R.raw (in settings)
1. don't allow ' character in city name and commas in description
1. adjust column width in my trips
1. Add a loading bar when reading csv files
1. Add table cityloc as menu item
1. Add "home" option in settings
1. Add TextWatcher to from/to City/Country - set long and lat to blank if text is changed
1. When returning to TripListView -> have year extended
1. Check in multi stop trip that dates are sequential
