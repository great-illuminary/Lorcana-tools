package eu.codlab.lexer.actions

import eu.codlab.lorcana.raw.RawVirtualCard
import eu.codlab.lorcana.raw.VariantClassification
import eu.codlab.lorcana.raw.VariantString
import eu.codlab.lorcana.raw.VirtualCard

internal interface ApplyAction {
    fun apply(card: VirtualCard, variant: VariantClassification, value: String): Boolean

    fun apply(card: RawVirtualCard, variant: VariantString, value: String): Boolean
}
