package eu.codlab.maps

import kotlinx.serialization.Serializable

@Serializable
data class LatLng(
    val latitude: Double,
    val longitude: Double
) {
    fun toXY() = MapUtils.latLngToXY(this)

    fun distance(latLng: LatLng) = MapUtils.distance(this, latLng)
}
