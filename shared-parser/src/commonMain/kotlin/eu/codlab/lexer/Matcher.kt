package eu.codlab.lexer

import eu.codlab.lorcana.raw.RawVirtualCard
import eu.codlab.lorcana.raw.VariantClassification
import eu.codlab.lorcana.raw.VariantString
import eu.codlab.lorcana.raw.VirtualCard

class Matcher internal constructor(internal val expression: Expression) {
    fun match(virtualCard: VirtualCard) =
        null != virtualCard.variants.find { match(virtualCard, it) }

    fun match(virtualCard: VirtualCard, variant: VariantClassification) =
        expression.apply(virtualCard, variant)

    fun match(virtualCard: RawVirtualCard) =
        null != virtualCard.variants.find { match(virtualCard, it) }

    fun match(virtualCard: RawVirtualCard, variant: VariantString) =
        expression.apply(virtualCard, variant)
}
