package eu.codlab.lexer

import eu.codlab.lorcana.Lorcana
import kotlinx.coroutines.test.runTest
import kotlinx.serialization.json.Json
import kotlin.test.Test

class TestCommand {
    @Test
    fun testLexer() = runTest {
        val lorcana = Lorcana()
        val loaded = lorcana.loadFromResources()
        val parser = Parser()

        val matcher = parser.parse("(set:tfc & id:10) | (set:rotf & id:20)")

        val filtered = loaded.cards.filter { matcher.match(it) }
        println("found cards ${filtered.size}")
        filtered.forEach {
            println("card $it")
        }
    }
}