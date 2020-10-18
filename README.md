# Travel app for Android
Android App that stores trips and shows interesting statistics of your travel history

## Getting started
1. Open project in Android Studio
1. Get hold of the following files:
    - countries_continents.csv: contains a list with all countries in the world and in which continent
    - city_location.csv: contains a list of thousands of cities around the world with their latitude and longitude
    - If applicable, trips.csv: contains the history of trips you have added over time
1. Copy files into the Android Studio folder `res/raw`
1. Add an optional image named add_trip.png to folder `res/mipmap`
1. Install app on device
1. Go to Settings and click "import geo data"
1. If applicable, click "import trips history"
1. Go to Add Trip and start adding trips

## Exporting data
1. Go to settings and click "export all data"
1. Look for the following files in the shown device location:
    - trips.csv: contains the history of trips you have added over time
    - countries_continents.csv: contains a list with all countries in the world and in which continent
    - city_location.csv: contains a list of thousands of cities around the world with their latitude and longitude

## Technologies used
- Java
- Android Studio
- SQLite
