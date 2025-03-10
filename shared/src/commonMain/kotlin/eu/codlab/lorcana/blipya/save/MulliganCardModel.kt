package eu.codlab.lorcana.blipya.save

import kotlinx.serialization.Serializable

@Serializable
data class MulliganCardModel(
    val id: String,
    val name: String,
    val amount: Long
)
