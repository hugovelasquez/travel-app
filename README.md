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
1. Add table cityloc as menu item
1. Export triplist, worldcities, citiescontinents as `*.csv`
1. Import csv files in app (in settings), not via R.raw
1. Add a loading bar when reading csv files
1. Bar Chart: add dropdown option (select overall, last 2, 5, or 10 years)
1. Bar Chart: stack per period (2008-2012: color 1, 20013-2017: color 2, etc.)
1. Bubble Chart: include visited countries in tooltip
1. Tag Cloud: show corresponding trips when clicking on country or city
1. Calculate DB statistics just once in TripStatisticsFragment and pass it on to Chart Fragments
1. Add "home" option in settings
1. When returning to TripListView -> have year extended
1. Check in multi stop trip that dates are sequential
1. Final Refactoring
