package eu.codlab.lexer

import kotlinx.serialization.json.Json
import kotlin.test.Test

class TestLexer {
    @Test
    fun testLexerUsingSomeExpectedLookup() {
        val parser = Parser()
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
                println(
                    json.encodeToString(
                        Expression.serializer(),
                        parser.parse(it).expression
                    )
                )
            } catch (err: Throwable) {
                err.printStackTrace()
                throw err
            }
            println("-----------------------------")
        }
    }
}
