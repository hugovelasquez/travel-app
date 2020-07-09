package com.sucaldo.travelapp.model;

public class DistanceCalculator {

    // static --> methods can be used without instantiating
    public static long getDistanceFromLatLongInKms(float lat1, float long1, float lat2, float long2) {
        int radiusEarth = 6371;
        double dLat = degToRadius(lat2 - lat1);
        double dLong = degToRadius(long2 - long1);
        double a =
                Math.sin(dLat / 2) * Math.sin(dLat / 2) +
                        Math.cos(degToRadius(lat1)) * Math.cos(degToRadius(lat2)) *
                                Math.sin(dLong / 2) * Math.sin(dLong / 2);
        double c = 2 * Math.atan2(Math.sqrt(a), Math.sqrt(1 - a));
        double d = radiusEarth * c;
        return Math.round(d);
    }

    private static double degToRadius(float deg) {
        return deg * (Math.PI / 180);
    }

}
