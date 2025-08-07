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

@Suppress("MagicNumber")
object MapUtils {
    private const val EARTH_RADIUS_KM = 6371

    fun xyToLatLng(x: Double, y: Double): LatLng {
        val long = x * 360 - 180
        val lat = atan(sinh(PI * (1 - 2 * y))).rad2deg()

        return LatLng(latitude = lat, longitude = long)
    }

    fun latLngToXY(latLng: LatLng) = latLngToXY(latLng.latitude, latLng.longitude)

    fun latLngToXY(lat: Double, long: Double): XY {
        val x = (long + 180) / 360

        val y = (1 - (ln(tan(lat.deg2rad()) + 1 / (cos(lat.deg2rad()))) / PI)) / 2

        return XY(x = x, y = y)
    }

    fun delta(center: LatLng, distanceMeter: Int, bearing: Double): LatLng {
        val latitudeRadians = center.latitude.deg2rad()
        val longitudeRadians = center.longitude.deg2rad()
        val radiusRadian = distanceMeter * 1.0 / 1000.0 / EARTH_RADIUS_KM
        val d = radiusRadian

        var pLat =
            asin(sin(latitudeRadians) * cos(d) + cos(latitudeRadians) * sin(d) * cos(bearing))
        val pLng = (longitudeRadians + atan2(
            sin(bearing) * sin(d) * cos(latitudeRadians),
            cos(d) - sin(latitudeRadians) * sin(pLat)
        )).rad2deg()
        pLat = pLat.rad2deg()

        return LatLng(pLat, pLng)
    }

    fun distance(latLng: LatLng, latLng2: LatLng) =
        distance(latLng.latitude, latLng.longitude, latLng2.latitude, latLng2.longitude)

    fun distance(lat1: Double, lon1: Double, lat2: Double, lon2: Double): Double {
        val theta = lon1 - lon2
        var dist = sin(lat1.deg2rad()) * sin(lat2.deg2rad()) + cos(
            lat1.deg2rad()
        ) * cos(lat2.deg2rad()) * cos(theta.deg2rad())
        dist = acos(dist)
        dist = dist.rad2deg()
        dist *= 60 * 1.1515
        dist *= 1.609344
        return (dist)
    }

    fun circle(center: LatLng, radiusMeter: Int) = circle(center, radiusMeter) { this }

    fun <T> circle(
        center: LatLng,
        radiusMeter: Int,
        transform: LatLng.() -> T
    ): List<T>? {
        if (radiusMeter <= 0) return null

        val latitudeRadians = center.latitude.deg2rad()
        val longitudeRadians = center.longitude.deg2rad()
        val radiusRadian = radiusMeter * 1.0 / 1000.0 / EARTH_RADIUS_KM
        val d = radiusRadian

        val points = (0..<361 step 1).map { angle ->
            val bearing = angle.deg2rad()

            var pLat =
                asin(sin(latitudeRadians) * cos(d) + cos(latitudeRadians) * sin(d) * cos(bearing))
            val pLng = (longitudeRadians + atan2(
                sin(bearing) * sin(d) * cos(latitudeRadians),
                cos(d) - sin(latitudeRadians) * sin(pLat)
            )).rad2deg()
            pLat = pLat.rad2deg()

            LatLng(pLat, pLng).transform()
        }

        return points
    }

    private fun Double.rad2deg() = this * 180.0 / PI

    private fun Double.deg2rad() = this * PI / 180.0

    private fun Int.deg2rad() = this * PI / 180.0
}
