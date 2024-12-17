package eu.codlab.lorcana.blipya.dreamborn

import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable
import kotlinx.serialization.Transient

@Serializable
data class Deck(
    val uuid: String,
    @SerialName("updated_at")
    val updatedAt: String,
    val creator: String,
    @SerialName("creator_name")
    val creatorName: String,
    val youtube: String?,
    val name: String,
    val cardsCount: Long?,
    val views: Long?,
    val likes: Long?,
    var cards: List<DeckCards> = emptyList()
)
