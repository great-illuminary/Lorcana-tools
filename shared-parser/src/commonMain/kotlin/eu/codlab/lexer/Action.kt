package eu.codlab.lexer

import eu.codlab.lexer.actions.ApplyAction
import eu.codlab.lorcana.raw.RawVirtualCard
import eu.codlab.lorcana.raw.VariantClassification
import eu.codlab.lorcana.raw.VariantString
import eu.codlab.lorcana.raw.VirtualCard

enum class Action(private val apply: ApplyAction) : ApplyAction {
    Artist(eu.codlab.lexer.actions.Artist),
    Attack(eu.codlab.lexer.actions.Attack),
    Cost(eu.codlab.lexer.actions.Cost),
    Defence(eu.codlab.lexer.actions.Defence),
    Id(eu.codlab.lexer.actions.Id),
    Lore(eu.codlab.lexer.actions.Lore),
    Move(eu.codlab.lexer.actions.Move),
    Rarity(eu.codlab.lexer.actions.Rarity),
    Set(eu.codlab.lexer.actions.Set),
    Ink(eu.codlab.lexer.actions.Ink),
    VariantSet(eu.codlab.lexer.actions.VariantSet);

    override fun apply(card: RawVirtualCard, variant: VariantString, value: String) =
        apply.apply(card, variant, value)

    override fun apply(card: VirtualCard, variant: VariantClassification, value: String) =
        apply.apply(card, variant, value)

    companion object {
        private val cache = Action.entries.associateBy { it.name.lowercase() }

        fun String.toAction() = cache[this]
    }
}
