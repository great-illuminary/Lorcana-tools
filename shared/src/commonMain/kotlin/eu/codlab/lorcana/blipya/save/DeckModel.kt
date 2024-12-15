package eu.codlab.lorcana.blipya.save

import kotlinx.serialization.Serializable

@Serializable
data class DeckModel(
    val id: String,
    val name: String,
    val size: Long,
    val hand: Long,
    val scenarii: List<ScenarioModel> = emptyList()
)

@Serializable
data class ScenarioModel(
    val id: String,
    val name: String,
    val cards: List<ExpectedCardModel>
)
