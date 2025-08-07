package eu.codlab.lexer.actions

import eu.codlab.lorcana.cards.InkColor
import eu.codlab.lorcana.raw.RawVirtualCard
import eu.codlab.lorcana.raw.VariantClassification
import eu.codlab.lorcana.raw.VariantString
import eu.codlab.lorcana.raw.VirtualCard

internal object Ink : ApplyAction {
    override fun apply(
        card: VirtualCard,
        variant: VariantClassification,
        value: String
    ) = card.colors.contains(value.toInkColor())

    override fun apply(
        card: RawVirtualCard,
        variant: VariantString,
        value: String
    ) =
        card.colors.contains(value.toInkColor())

    private val colorMap = InkColor.entries.associateBy { it.name.lowercase() }

    private fun String.toInkColor() = colorMap[this]
}
