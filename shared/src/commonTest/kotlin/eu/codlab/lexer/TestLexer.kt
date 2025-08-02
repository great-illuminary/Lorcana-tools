package eu.codlab.lexer

import guru.zoroark.lixy.LixyTokenType
import guru.zoroark.lixy.lixy
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
    Or,
    And,
    RegularToken
}

@Serializable
sealed class Expression : PangoroNode {
    companion object : PangoroNodeDeclaration<Expression> {
        override fun make(args: PangoroTypeDescription): Expression {
            if (args.arguments.containsKey("notExpr")) return Not(args["notExpr"])

            if (args.arguments.containsKey("orRight")) return Or(
                left = args["expr"],
                right = args["orRight"]
            )

            if (args.arguments.containsKey("andRight")) return And(
                left = args["expr"],
                right = args["andRight"]
            )

            if (!args.arguments.containsKey("expr")) return Empty()
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
            if (args.arguments.containsKey("notExpr")) return Not(args["notExpr"])

            if (args.arguments.containsKey("orRight")) return Not(
                Or(
                    left = args["expr"],
                    right = args["orRight"]
                )
            )

            if (args.arguments.containsKey("andRight")) return Not(
                And(
                    left = args["expr"],
                    right = args["andRight"]
                )
            )

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
data class Or(
    val left: Expression,
    val right: Expression
) : Expression() {
    companion object : PangoroNodeDeclaration<Or> {
        override fun make(args: PangoroTypeDescription) = Or(args["left"], args["right"])
    }
}

@Serializable
data class And(
    val left: Expression,
    val right: Expression
) : Expression() {
    companion object : PangoroNodeDeclaration<And> {
        override fun make(args: PangoroTypeDescription) = And(args["left"], args["right"])
    }
}

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
            if (args.arguments.containsKey("orRight")) return Or(
                left = actualValue,
                right = args["orRight"]
            )

            if (args.arguments.containsKey("andRight")) return And(
                left = actualValue,
                right = args["andRight"]
            )

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
            "|" isToken LorcanaTokenTypes.Or
            "&" isToken LorcanaTokenTypes.And
            matches("[A-Za-z0-9]+") isToken LorcanaTokenTypes.RegularToken
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
                expect(LorcanaTokenTypes.And)
                expect(Expression) storeIn "andRight"
            } or {
                expect(Not) storeIn "expr"
                expect(LorcanaTokenTypes.Or)
                expect(Expression) storeIn "orRight"
            } or {
                expect(LorcanaTokenTypes.OpenParenthesis)
                expect(Expression) storeIn "expr"
                expect(LorcanaTokenTypes.CloseParenthesis)
                expect(LorcanaTokenTypes.And)
                expect(Expression) storeIn "andRight"
            } or {
                expect(LorcanaTokenTypes.OpenParenthesis)
                expect(Expression) storeIn "expr"
                expect(LorcanaTokenTypes.CloseParenthesis)
                expect(LorcanaTokenTypes.Or)
                expect(Expression) storeIn "orRight"
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
                expect(LorcanaTokenTypes.And)
                expect(Expression) storeIn "andRight"
            } or {
                expect(LorcanaTokenTypes.Not)
                expect(LorcanaTokenTypes.OpenParenthesis)
                expect(Expression) storeIn "expr"
                expect(LorcanaTokenTypes.CloseParenthesis)
                expect(LorcanaTokenTypes.Or)
                expect(Expression) storeIn "orRight"
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
                expect(LorcanaTokenTypes.And)
                expect(Expression) storeIn "andRight"
            } or {
                expect(LorcanaTokenTypes.RegularToken) storeIn "value"
                expect(LorcanaTokenTypes.Or)
                expect(Expression) storeIn "orRight"
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
            "(1 & 3) | (!test & (19e | oeuhd))"
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