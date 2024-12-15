package eu.codlab.lorcana.blipya.save

import kotlinx.serialization.Serializable

@Serializable
data class ConfigurationModel(
    val decks: List<DeckModel> = listOf()
)
