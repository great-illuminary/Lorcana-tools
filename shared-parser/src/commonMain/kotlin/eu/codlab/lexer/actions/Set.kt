package eu.codlab.lexer.actions

import eu.codlab.lorcana.raw.RawVirtualCard
import eu.codlab.lorcana.raw.SetDescription
import eu.codlab.lorcana.raw.VariantClassification
import eu.codlab.lorcana.raw.VariantString
import eu.codlab.lorcana.raw.VirtualCard

internal object Set : ApplyAction {
    override fun apply(card: VirtualCard, variant: VariantClassification, value: String) =
        variant.set == value.toSetDescription()

    override fun apply(card: RawVirtualCard, variant: VariantString, value: String) =
        variant.set == value.toSetDescription()

    private fun String.toSetDescription() =
        SetDescription.entries.find { it.name.lowercase() == this.lowercase() }
}
