package eu.codlab.lorcana.blipya.rph.models

import kotlinx.serialization.Serializable

@Serializable
data class User(
    val id: Long,
    val bestIdentifier: String? = null,
    val bestIdentifierInGame: String? = null,
)