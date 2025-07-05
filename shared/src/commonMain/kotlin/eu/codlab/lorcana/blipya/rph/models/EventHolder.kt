package eu.codlab.lorcana.blipya.rph.models

import kotlinx.serialization.Serializable

@Serializable
data class EventHolder(
    val event: Event,
    val store: Store? = null
) {
    fun latLng() = event.latLng() ?: store?.latLng()
}
