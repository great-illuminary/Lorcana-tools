package eu.codlab.lorcana.blipya.deck.card

import eu.codlab.lorcana.raw.VariantClassification
import eu.codlab.lorcana.raw.VirtualCard

data class VirtualCardNumber(
    val card: VirtualCard,
    val variant: VariantClassification,
    val number: Int
)
