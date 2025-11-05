package eu.codlab.lexer.actions

import eu.codlab.lorcana.raw.*

internal object VariantSet : ApplyAction {
    override fun apply(card: VirtualCard, variant: VariantClassification, value: String) =
        null != card.variants.find { (it.set to it.id).same(value) }

    override fun apply(card: RawVirtualCard, variant: VariantString, value: String) =
        null != card.variants.find { (it.set to it.id).same(value) }

    private fun String.toPair() = split(",").let {
        it[0].toSetDescription() to it.getOrNull(1)?.toIntOrNull()
    }

    private fun Pair<SetDescription, Int>.same(value: String): Boolean {
        val (set, id) = value.toPair()

        if (set != first) return false
        // or we have the same set and no id OR we check the id
        return null == id || id == second
    }

    private fun String.toSetDescription() =
        SetDescription.entries.find { it.name.equals(this, ignoreCase = true) }
}
