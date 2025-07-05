package eu.codlab.lorcana.blipya.rph.models

import eu.codlab.maps.LatLng

interface Positionable {
    val latitude: Double?
    val longitude: Double?

    fun latLng() = (latitude to longitude).let { (lat, long) ->
        if (null == lat || null == long) {
            null
        } else {
            LatLng(
                latitude = lat,
                longitude = long
            )
        }
    }
}