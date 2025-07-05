package eu.codlab.maps

import kotlin.math.PI
import kotlin.math.acos
import kotlin.math.asin
import kotlin.math.atan
import kotlin.math.atan2
import kotlin.math.cos
import kotlin.math.ln
import kotlin.math.sin
import kotlin.math.sinh
import kotlin.math.tan

object MapUtils {
    private val EARTH_RADIUS_KM = 6371

    fun xyToLatLng(x: Double, y: Double): LatLng {
        val long = x * 360 - 180
        val lat = atan(sinh(PI * (1 - 2 * y))) * 180 / PI

        return LatLng(latitude = lat, longitude = long)
    }

    fun latLngToXY(latLng: LatLng) = latLngToXY(latLng.latitude, latLng.longitude)

    fun latLngToXY(lat: Double, long: Double): XY {
        val x = (long + 180) / 360

        val y = (1 - (ln(tan(lat * PI / 180) + 1 / (cos(lat * PI / 180))) / PI)) / 2

        return XY(x = x, y = y)
    }

    fun delta(center: LatLng, distanceMeter: Int, bearing: Double): LatLng {
        val latitudeRadians = center.latitude * PI / 180.0
        val longitudeRadians = center.longitude * PI / 180.0
        val radiusRadian = distanceMeter * 1.0 / 1000.0 / EARTH_RADIUS_KM
        val d = radiusRadian

        var pLat =
            asin(sin(latitudeRadians) * cos(d) + cos(latitudeRadians) * sin(d) * cos(bearing))
        val pLng = (longitudeRadians + atan2(
            sin(bearing) * sin(d) * cos(latitudeRadians),
            cos(d) - sin(latitudeRadians) * sin(pLat)
        )) * 180 / PI
        pLat = pLat * 180 / PI

        return LatLng(pLat, pLng)
    }

    fun distance(latLng: LatLng, latLng2: LatLng) =
        distance(latLng.latitude, latLng.longitude, latLng2.latitude, latLng2.longitude)

    fun distance(lat1: Double, lon1: Double, lat2: Double, lon2: Double): Double {
        val theta = lon1 - lon2
        var dist = sin(deg2rad(lat1)) * sin(deg2rad(lat2)) + cos(
            deg2rad(lat1)
        ) * cos(deg2rad(lat2)) * cos(deg2rad(theta))
        dist = acos(dist)
        dist = rad2deg(dist)
        dist *= 60 * 1.1515
        dist *= 1.609344
        return (dist)
    }

    fun circle(center: LatLng, radiusMeter: Int) = circle(center, radiusMeter) { this }

    fun toRadian(theta: Double) = theta * PI / 180.0

    fun <T> circle(
        center: LatLng,
        radiusMeter: Int,
        transform: LatLng.() -> T
    ): List<T>? {
        if (radiusMeter <= 0) return null

        val latitudeRadians = center.latitude * PI / 180.0
        val longitudeRadians = center.longitude * PI / 180.0
        val radiusRadian = radiusMeter * 1.0 / 1000.0 / EARTH_RADIUS_KM
        val d = radiusRadian

        val points = (0..<361 step 1).map { angle ->
            val bearing = angle * PI / 180.0

            var pLat =
                asin(sin(latitudeRadians) * cos(d) + cos(latitudeRadians) * sin(d) * cos(bearing))
            val pLng = (longitudeRadians + atan2(
                sin(bearing) * sin(d) * cos(latitudeRadians),
                cos(d) - sin(latitudeRadians) * sin(pLat)
            )) * 180 / PI
            pLat = pLat * 180 / PI

            LatLng(pLat, pLng).transform()
        }

        return points
    }


    private fun deg2rad(deg: Double): Double {
        return (deg * PI / 180.0)
    }

    private fun rad2deg(rad: Double): Double {
        return (rad * 180.0 / PI)
    }
}