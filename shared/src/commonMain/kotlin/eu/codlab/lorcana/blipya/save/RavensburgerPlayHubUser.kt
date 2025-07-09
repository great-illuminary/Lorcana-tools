package eu.codlab.lorcana.blipya.save

import kotlinx.serialization.Serializable

@Serializable
data class RavensburgerPlayHubUser(
    val username: String,
    val id: Long
)
