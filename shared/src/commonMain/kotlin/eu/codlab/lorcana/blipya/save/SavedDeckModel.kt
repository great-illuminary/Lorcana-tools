package eu.codlab.lorcana.blipya.save

import eu.codlab.lorcana.blipya.dreamborn.Deck
import kotlinx.serialization.Serializable

@Serializable
data class SavedDeckModel(
    val id: String,
    val version: Int = 0,
    val name: String,
    val size: Long,
    val hand: Long,
    val scenarii: List<ScenarioModel> = emptyList(),
    val dreambornDeck: Dreamborn? = null
)

@Serializable
data class Dreamborn(
    val url: String,
    val data: Deck? = null
)

@Serializable
data class ScenarioModel(
    val id: String,
    val name: String,
    val cards: List<ExpectedCardModel>
)
