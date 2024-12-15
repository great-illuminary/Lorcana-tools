package eu.codlab.lorcana.blipya.save

import kotlinx.serialization.Serializable

@Serializable
data class ExpectedCardModel(
    val id: String,
    val name: String,
    val amount: Long,
    val min: Long,
    val max: Long
)
