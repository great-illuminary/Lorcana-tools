package eu.codlab.lorcana.blipya.rph.models

import kotlinx.serialization.Serializable

@Serializable
data class StoreHolder(
    val store: Store
) {
    fun latLng() = store.latLng()
}
