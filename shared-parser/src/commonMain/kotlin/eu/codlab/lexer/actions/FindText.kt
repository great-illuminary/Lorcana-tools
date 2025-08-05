package eu.codlab.lexer.actions

import eu.codlab.lorcana.cards.CardTranslation
import eu.codlab.lorcana.cards.CardTranslations
import eu.codlab.lorcana.raw.RawVirtualCard
import eu.codlab.lorcana.raw.VariantClassification
import eu.codlab.lorcana.raw.VariantString
import eu.codlab.lorcana.raw.VirtualCard

object FindText : ApplyAction {
    override fun apply(card: VirtualCard, variant: VariantClassification, value: String): Boolean {
        return card.languages.contains(value)
    }

    override fun apply(card: RawVirtualCard, variant: VariantString, value: String): Boolean {
        return card.languages.contains(value)
    }

    private fun CardTranslations.contains(value: String) =
        toFullString().lowercase().contains(value.lowercase())

    private fun CardTranslations.toFullString() =
        listOfNotNull(it, de, en, fr, ja, zh).joinToString("") { it.toFullString() }

    private fun CardTranslation.toFullString() =
        listOfNotNull(name, title, flavour).joinToString("")
}
