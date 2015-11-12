package com.dg.helpers;

/**
 * Created by Daniel Cohen Gindi (danielgindi@gmail.com)
 */
public class LocationHelper
{
    /**
     * Calculates the distance between the specified locations
     * @param lat1
     * @param lon1
     * @param lat2
     * @param lon2
     * @return Distance in kilometers
     */
    public static double getDistanceBetweenCoordinates(
            double lat1, double lon1,
            double lat2, double lon2)
    {
        final double R = 6371.0;
        final double M_PI = Math.PI;
        double DEG_TO_RAD = (M_PI / 180.0);
        lat1 *= DEG_TO_RAD;
        lon1 *= DEG_TO_RAD;
        lat2 *= DEG_TO_RAD;
        lon2 *= DEG_TO_RAD;
        double latDelta = lat2 - lat1;
        double longitudeDelta = lon2 - lon1;

        double a = Math.sin(latDelta/2.0) * Math.sin(latDelta/2.0) +
                Math.cos(lat1) * Math.cos(lat2) *
                        Math.sin(longitudeDelta/ 2.0) * Math.sin(longitudeDelta/2.0);
        double c = 2.0 * Math.atan2(Math.sqrt(a), Math.sqrt(1.0-a));
        return R * c; // Kilometers
    }
}