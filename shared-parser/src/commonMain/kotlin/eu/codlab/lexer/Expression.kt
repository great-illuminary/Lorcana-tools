package eu.codlab.lexer

import eu.codlab.lexer.Action.Companion.toAction
import eu.codlab.lexer.actions.FindText
import eu.codlab.lorcana.raw.RawVirtualCard
import eu.codlab.lorcana.raw.VariantClassification
import eu.codlab.lorcana.raw.VariantString
import eu.codlab.lorcana.raw.VirtualCard
import guru.zoroark.pangoro.PangoroNode
import guru.zoroark.pangoro.PangoroNodeDeclaration
import guru.zoroark.pangoro.PangoroTypeDescription
import korlibs.io.lang.substr
import kotlinx.serialization.Serializable

@Serializable
sealed class Expression : PangoroNode {
    abstract fun apply(card: VirtualCard, variant: VariantClassification): Boolean

    abstract fun apply(card: RawVirtualCard, variant: VariantString): Boolean

    companion object : PangoroNodeDeclaration<Expression> {
        override fun make(args: PangoroTypeDescription): Expression {
            if (args.arguments.containsKey("right")) {
                val operator: String = args["op"]

                return if (operator == "&") {
                    And(args["expr"], args["right"])
                } else {
                    Or(args["expr"], args["right"])
                }
            }

            return args["expr"]
        }
    }
}

@Serializable
data class Empty(
    val empty: Boolean = true
) : Expression() {
    override fun apply(card: VirtualCard, variant: VariantClassification) = false

    override fun apply(card: RawVirtualCard, variant: VariantString) = false
}

@Serializable
data class Not(
    val expression: Expression
) : Expression() {
    companion object : PangoroNodeDeclaration<Expression> {
        override fun make(args: PangoroTypeDescription): Expression {
            if (args.arguments.containsKey("right")) {
                val operator: String = args["op"]

                return Not(
                    if (operator == "&") {
                        And(args["left"], args["right"])
                    } else {
                        Or(args["left"], args["right"])
                    }
                )
            }

            return when (val result: Expression = args["expr"]) {
                is And -> And(Not(result.left), result.right)
                is Empty -> Not(result)
                is Not -> Not(result)
                is Or -> Or(Not(result.left), result.right)
                is Regular -> Not(result)
                is Filter -> Not(result)
            }
        }
    }

    override fun apply(card: VirtualCard, variant: VariantClassification) =
        !expression.apply(card, variant)

    override fun apply(card: RawVirtualCard, variant: VariantString) =
        !expression.apply(card, variant)
}

@Serializable
sealed class ComparatorExpression : Expression() {
    companion object : PangoroNodeDeclaration<ComparatorExpression> {
        override fun make(args: PangoroTypeDescription): ComparatorExpression {
            val operator: String = args["op"]
            return if (operator == "&") {
                And(args["left"], args["right"])
            } else {
                Or(args["left"], args["right"])
            }
        }
    }
}

@Serializable
data class Filter(
    val action: Action? = null,
    val filter: String
) : Expression() {
    companion object : PangoroNodeDeclaration<Filter> {
        override fun make(args: PangoroTypeDescription): Filter {
            val filter = args.get<String>("filter").let {
                if (it.startsWith("\"") &&
                    it.endsWith("\"") &&
                    it.length > 2
                ) {
                    it.substr(1, it.length - 2)
                } else {
                    it
                }
            }

            return Filter(
                action = if (args.arguments.containsKey("action")) {
                    args.get<String>("action").toAction()
                } else {
                    null
                },
                filter = filter
            )
        }
    }

    override fun apply(card: VirtualCard, variant: VariantClassification) =
        (action ?: FindText).apply(card, variant, filter)

    override fun apply(card: RawVirtualCard, variant: VariantString) =
        (action ?: FindText).apply(card, variant, filter)
}

@Serializable
data class Or(
    val left: Expression,
    val right: Expression
) : ComparatorExpression() {
    override fun apply(card: VirtualCard, variant: VariantClassification) =
        left.apply(card, variant) || right.apply(card, variant)

    override fun apply(card: RawVirtualCard, variant: VariantString) =
        left.apply(card, variant) || right.apply(card, variant)
}

@Serializable
data class And(
    val left: Expression,
    val right: Expression
) : ComparatorExpression() {
    override fun apply(card: VirtualCard, variant: VariantClassification) =
        left.apply(card, variant) && right.apply(card, variant)

    override fun apply(card: RawVirtualCard, variant: VariantString) =
        left.apply(card, variant) && right.apply(card, variant)
}

@Serializable
data class Regular(
    val value: Filter
) : Expression() {
    companion object : PangoroNodeDeclaration<Expression> {
        override fun make(args: PangoroTypeDescription): Expression {
            if (args.arguments.containsKey("right")) {
                val operator: String = args["op"]
                return if (operator == "&") {
                    And(Regular(args["value"]), args["right"])
                } else {
                    Or(Regular(args["value"]), args["right"])
                }
            }

            return Regular(args["value"])
        }
    }

    override fun apply(card: VirtualCard, variant: VariantClassification) =
        value.apply(card, variant)

    override fun apply(card: RawVirtualCard, variant: VariantString) = value.apply(card, variant)
}
