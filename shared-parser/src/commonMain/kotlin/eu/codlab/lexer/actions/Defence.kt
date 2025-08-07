package eu.codlab.lexer.actions

import eu.codlab.lorcana.raw.RawVirtualCard
import eu.codlab.lorcana.raw.VariantClassification
import eu.codlab.lorcana.raw.VariantString
import eu.codlab.lorcana.raw.VirtualCard

internal object Defence : ApplyAction {
    override fun apply(card: VirtualCard, variant: VariantClassification, value: String) =
        card.defence == value.toInt()

    override fun apply(card: RawVirtualCard, variant: VariantString, value: String) =
        card.defence == value.toInt()
}
