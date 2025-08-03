package eu.codlab.lexer.actions

import eu.codlab.lorcana.raw.RawVirtualCard
import eu.codlab.lorcana.raw.VariantClassification
import eu.codlab.lorcana.raw.VariantString
import eu.codlab.lorcana.raw.VirtualCard

internal object Id : ApplyAction {
    override fun apply(card: VirtualCard, variant: VariantClassification, value: String) =
        variant.id == value.toInt()

    override fun apply(card: RawVirtualCard, variant: VariantString, value: String) =
        variant.id == value.toInt()
}
