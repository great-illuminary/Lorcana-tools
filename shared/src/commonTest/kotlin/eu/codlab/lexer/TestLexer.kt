package eu.codlab.lexer

import guru.zoroark.lixy.LixyTokenType
import guru.zoroark.lixy.lixy
import guru.zoroark.lixy.matchers.anyOf
import guru.zoroark.lixy.matchers.matches
import guru.zoroark.pangoro.PangoroNode
import guru.zoroark.pangoro.PangoroNodeDeclaration
import guru.zoroark.pangoro.PangoroTypeDescription
import guru.zoroark.pangoro.dsl.either
import guru.zoroark.pangoro.dsl.expect
import guru.zoroark.pangoro.dsl.or
import guru.zoroark.pangoro.dsl.pangoro
import kotlinx.serialization.Serializable
import kotlinx.serialization.json.Json
import kotlin.test.Test

enum class LorcanaTokenTypes : LixyTokenType {
    Not,
    OpenParenthesis,
    CloseParenthesis,
    Comparator,
    RegularToken
}

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
    val value: String
) : Expression() {
    companion object : PangoroNodeDeclaration<Expression> {
        override fun make(args: PangoroTypeDescription): Expression {
            val actualValue = if (args.arguments.containsKey("notValue")) {
                Not(Regular(args["notValue"]))
            } else {
                Regular(args["value"])
            }

            if (args.arguments.containsKey("right")) {
                val operator: String = args["op"]
                return if (operator == "&") {
                    And(actualValue, args["right"])
                } else {
                    Or(actualValue, args["right"])
                }
            }

            return actualValue
        }
    }
}

class TestLexer {
    val lexer = lixy {
        state {
            "!" isToken LorcanaTokenTypes.Not
            "(" isToken LorcanaTokenTypes.OpenParenthesis
            ")" isToken LorcanaTokenTypes.CloseParenthesis
            anyOf("|", "&") isToken LorcanaTokenTypes.Comparator
            matches("(\"[^\"\\(\\)\\!&\\|]*\")|([^\"\\ \\(\\)\\!&\\|]+(\"[^\"\\(\\)\\!&\\|]*\")?)") isToken LorcanaTokenTypes.RegularToken
            " ".ignore
        }
    }

    // ideally it would be using a lighter parser afterward but the flexibility with the parenthesis
    // would create parsing issues, to prevent this, for now, the various expressions
    // will be able to carry out specific management -> which will be able to be changed tho with
    // sub-partial branches
    val parser = pangoro {
        Expression root {
            either {
                expect(Not) storeIn "expr"
                expect(LorcanaTokenTypes.Comparator) storeIn "op"
                expect(Expression) storeIn "right"
            } or {
                expect(LorcanaTokenTypes.OpenParenthesis)
                expect(Expression) storeIn "expr"
                expect(LorcanaTokenTypes.CloseParenthesis)
                expect(LorcanaTokenTypes.Comparator) storeIn "op"
                expect(Expression) storeIn "right"
            } or {
                expect(LorcanaTokenTypes.OpenParenthesis)
                expect(Expression) storeIn "expr"
                expect(LorcanaTokenTypes.CloseParenthesis)
            } or {
                expect(Not) storeIn "expr"
            } or {
                expect(Regular) storeIn "expr"
            }
        }
        Not {
            either {
                expect(LorcanaTokenTypes.Not)
                expect(LorcanaTokenTypes.OpenParenthesis)
                expect(Expression) storeIn "expr"
                expect(LorcanaTokenTypes.CloseParenthesis)
                expect(LorcanaTokenTypes.Comparator) storeIn "op"
                expect(Expression) storeIn "right"
            } or {
                expect(LorcanaTokenTypes.Not)
                expect(LorcanaTokenTypes.OpenParenthesis)
                expect(Expression) storeIn "expr"
                expect(LorcanaTokenTypes.CloseParenthesis)
            } or {
                expect(LorcanaTokenTypes.Not)
                expect(Not) storeIn "expr"
            } or {
                expect(LorcanaTokenTypes.Not)
                expect(Regular) storeIn "expr"
            }
        }
        Regular {
            either {
                expect(LorcanaTokenTypes.RegularToken) storeIn "value"
                expect(LorcanaTokenTypes.Comparator) storeIn "op"
                expect(Expression) storeIn "right"
            } or {
                expect(LorcanaTokenTypes.RegularToken) storeIn "value"
            }
        }
    }

    @Test
    fun testLexer() {
        val json = Json {
            prettyPrintIndent = "  "
            prettyPrint = true
        }
        listOf(
            "testing",
            "test | nothing",
            "!test",
            "!test & !!!!!nothing",
            "1 & 2 | 4",
            "(1 & 2)",
            "(1 & 2) | 3",
            "((1 & 3) | (!test & (19e | oeuhd)))",
            "(1 & 3) | (!test & (19e | oeuhd))",
            """(1 & "test" | wrong:lol) | cost:"peter pan""""
        ).forEach {
            println("managing $it ---------------------->")
            try {
                lexer.tokenize(it).also { tokens ->
                    println(
                        json.encodeToString(
                            Expression.serializer(),
                            parser.parse(tokens) as Expression
                        )
                    )
                }
            } catch (err: Throwable) {
                err.printStackTrace()
                throw err
            }
            println("-----------------------------")
        }
    }
}