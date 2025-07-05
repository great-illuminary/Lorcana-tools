package eu.codlab.lorcana.blipya.rph.models

import kotlinx.serialization.Serializable

@Serializable
data class Store(
    val id: Long,
    val name: String,
    val fullAddress: String? = null,
    val country: String? = null,
    val website: String? = null,
    override val latitude: Double? = null,
    override val longitude: Double? = null
) : Positionable