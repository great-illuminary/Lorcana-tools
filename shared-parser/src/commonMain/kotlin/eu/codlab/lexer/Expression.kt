package eu.codlab.lexer

import eu.codlab.lexer.Action.Companion.toAction
import guru.zoroark.pangoro.PangoroNode
import guru.zoroark.pangoro.PangoroNodeDeclaration
import guru.zoroark.pangoro.PangoroTypeDescription
import kotlinx.serialization.Serializable

@Serializable
sealed class Expression : PangoroNode {
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
) : Expression()

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
        override fun make(args: PangoroTypeDescription) = Filter(
            action = if (args.arguments.containsKey("action")) {
                args.get<String>("action").toAction()
            } else {
                null
            },
            filter = args["filter"]
        )
    }
}

@Serializable
data class Or(
    val left: Expression,
    val right: Expression
) : ComparatorExpression()

@Serializable
data class And(
    val left: Expression,
    val right: Expression
) : ComparatorExpression()

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
}
