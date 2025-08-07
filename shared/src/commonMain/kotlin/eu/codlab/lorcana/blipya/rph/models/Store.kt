package eu.codlab.lorcana.blipya.rph.models

import kotlinx.serialization.Serializable

@Serializable
data class Store(
    val uuid: String = "",
    val id: Long,
    val name: String,
    val fullAddress: String? = null,
    val country: String? = null,
    val website: String? = null,
    override val latitude: Double? = null,
    override val longitude: Double? = null,
    val email: String? = null,
    val streetAddress: String? = null,
    val zipcode: String? = null,
    val phoneNumber: String? = null,
    val storeTypes: List<String> = emptyList(),
    val storeTypesPretty: List<String> = emptyList(),
) : Positionable
