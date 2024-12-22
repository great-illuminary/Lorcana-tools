package eu.codlab.lorcana.blipya.save

import kotlinx.serialization.Serializable

@Serializable
data class ConfigurationModel(
    val decks: List<SavedDeckModel> = listOf(),
    val authentication: SavedAuthentication? = null
)
