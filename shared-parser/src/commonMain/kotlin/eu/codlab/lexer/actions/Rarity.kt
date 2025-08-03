package eu.codlab.lexer.actions

import eu.codlab.lorcana.cards.VariantRarity
import eu.codlab.lorcana.raw.RawVirtualCard
import eu.codlab.lorcana.raw.VariantClassification
import eu.codlab.lorcana.raw.VariantString
import eu.codlab.lorcana.raw.VirtualCard

internal object Rarity : ApplyAction {
    override fun apply(card: VirtualCard, variant: VariantClassification, value: String) =
        variant.rarity == value.toRarity()

    override fun apply(card: RawVirtualCard, variant: VariantString, value: String) =
        variant.rarity == value.toRarity()

    private fun String.toRarity() =
        VariantRarity.entries.find { it.name.lowercase() == this.lowercase() }
}
