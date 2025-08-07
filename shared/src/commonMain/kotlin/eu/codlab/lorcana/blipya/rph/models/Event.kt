package eu.codlab.lorcana.blipya.rph.models

import kotlinx.serialization.Serializable

@Serializable
data class Event(
    val id: Long,
    val name: String,
    val startDatetime: Long? = null,
    override val latitude: Double? = null,
    override val longitude: Double? = null
) : Positionable
