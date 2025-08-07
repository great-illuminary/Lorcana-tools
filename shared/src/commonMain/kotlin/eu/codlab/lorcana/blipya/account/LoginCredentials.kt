package eu.codlab.lorcana.blipya.account

import kotlinx.serialization.Serializable

@Serializable
data class LoginCredentials(
    val token: String,
    val expiresIn: Long
)
