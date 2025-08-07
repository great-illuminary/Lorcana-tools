package eu.codlab.lexer

import guru.zoroark.lixy.lixy
import guru.zoroark.lixy.matchers.anyOf
import guru.zoroark.lixy.matchers.matches
import guru.zoroark.pangoro.dsl.either
import guru.zoroark.pangoro.dsl.expect
import guru.zoroark.pangoro.dsl.or
import guru.zoroark.pangoro.dsl.pangoro

class Parser {
    private val lexer = lixy {
        state {
            ":" isToken LorcanaTokenTypes.Separator
            "!" isToken LorcanaTokenTypes.Not
            "(" isToken LorcanaTokenTypes.OpenParenthesis
            ")" isToken LorcanaTokenTypes.CloseParenthesis
            anyOf("|", "&") isToken LorcanaTokenTypes.Comparator
            matches("\"[^:\"]*\"") isToken LorcanaTokenTypes.DoubleQuote
            matches("""[^:" \(\)!&\|\n\t\r]+""") isToken LorcanaTokenTypes.RegularToken
            listOf(" ", "\t", "\n", "\r").forEach { it.ignore }
        }
    }

    private val actualParser = pangoro {
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
                expect(Filter) storeIn "value"
                expect(LorcanaTokenTypes.Comparator) storeIn "op"
                expect(Expression) storeIn "right"
            } or {
                expect(Filter) storeIn "value"
            }
        }
        Filter {
            either {
                expect(LorcanaTokenTypes.DoubleQuote) storeIn "filter"
            } or {
                expect(LorcanaTokenTypes.RegularToken) storeIn "action"
                expect(LorcanaTokenTypes.Separator)
                expect(LorcanaTokenTypes.DoubleQuote) storeIn "filter"
            } or {
                expect(LorcanaTokenTypes.RegularToken) storeIn "action"
                expect(LorcanaTokenTypes.Separator)
                expect(LorcanaTokenTypes.RegularToken) storeIn "filter"
            } or {
                expect(LorcanaTokenTypes.RegularToken) storeIn "filter"
            }
        }
    }

    fun parse(input: String) = Matcher(actualParser.parse(lexer.tokenize(input)) as Expression)
}
