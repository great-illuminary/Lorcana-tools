package eu.codlab.lorcana.blipya.dreamborn

import kotlinx.serialization.Serializable

@Serializable
data class Deck(
    val id: String,
    val url: String,
    var cards: Map<String, Int> = emptyMap()
) {
    val list = cards.map { (key, value) -> CardNumber(key, value) }
}

@Serializable
data class CardNumber(
    val card: String,
    val number: Int
)
