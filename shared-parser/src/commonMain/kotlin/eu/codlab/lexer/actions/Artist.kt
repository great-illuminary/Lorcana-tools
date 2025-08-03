package eu.codlab.lexer.actions

import eu.codlab.lorcana.raw.RawVirtualCard
import eu.codlab.lorcana.raw.VariantClassification
import eu.codlab.lorcana.raw.VariantString
import eu.codlab.lorcana.raw.VirtualCard

internal object Artist : ApplyAction {
    override fun apply(
        card: VirtualCard,
        variant: VariantClassification,
        value: String
    ) = card.illustrator.lowercase().contains(value.lowercase())

    override fun apply(card: RawVirtualCard, variant: VariantString, value: String) =
        card.illustrator.lowercase().contains(value.lowercase())
}