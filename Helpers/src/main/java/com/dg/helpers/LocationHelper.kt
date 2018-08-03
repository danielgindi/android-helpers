package com.dg.helpers

@Suppress("unused")
object LocationHelper
{
    private const val R = 6371.0
    private const val M_PI = Math.PI
    private const val DEG_TO_RAD = M_PI / 180.0

    /**
     * Calculates the distance between the specified locations
     * @param latitude1
     * @param longitude1
     * @param latitude2
     * @param longitude2
     * @return Distance in kilometers
     */
    fun getDistanceBetweenCoordinates(
            latitude1: Double, longitude1: Double,
            latitude2: Double, longitude2: Double): Double
    {
        var lat1 = latitude1
        var lon1 = longitude1
        var lat2 = latitude2
        var lon2 = longitude2

        lat1 *= DEG_TO_RAD
        lon1 *= DEG_TO_RAD
        lat2 *= DEG_TO_RAD
        lon2 *= DEG_TO_RAD
        val latDelta = lat2 - lat1
        val longitudeDelta = lon2 - lon1

        val a = Math.sin(latDelta / 2.0) * Math.sin(latDelta / 2.0) + Math.cos(lat1) * Math.cos(lat2) *
                Math.sin(longitudeDelta / 2.0) * Math.sin(longitudeDelta / 2.0)
        val c = 2.0 * Math.atan2(Math.sqrt(a), Math.sqrt(1.0 - a))
        return R * c // Kilometers
    }
}