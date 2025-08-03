package eu.codlab.lexer.actions

import eu.codlab.lorcana.raw.RawVirtualCard
import eu.codlab.lorcana.raw.VariantClassification
import eu.codlab.lorcana.raw.VariantString
import eu.codlab.lorcana.raw.VirtualCard

internal object Attack : ApplyAction {
    override fun apply(card: VirtualCard, variant: VariantClassification, value: String) =
        card.attack == value.toInt()

    override fun apply(card: RawVirtualCard, variant: VariantString, value: String) =
        card.attack == value.toInt()
}