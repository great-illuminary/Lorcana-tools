package eu.codlab.lorcana.blipya.save

import kotlinx.serialization.Serializable

@Serializable
data class SavedAuthentication(
    val token: String,
    val expiresAtMilliseconds: Long,
)
