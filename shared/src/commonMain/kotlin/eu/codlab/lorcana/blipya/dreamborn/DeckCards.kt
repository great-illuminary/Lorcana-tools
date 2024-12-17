package eu.codlab.lorcana.blipya.dreamborn

import kotlinx.serialization.Serializable
import kotlinx.serialization.Transient

@Serializable
data class DeckCards(
    @Transient
    val version: Long = 0,
    val dreamborn: String,
    val count: Long
)
