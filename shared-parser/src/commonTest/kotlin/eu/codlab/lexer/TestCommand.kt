package eu.codlab.lexer

import eu.codlab.lorcana.Lorcana
import kotlin.test.Test
import kotlinx.coroutines.test.runTest

class TestCommand {
    @Test
    fun testLexer() = runTest {
        val lorcana = Lorcana()
        val loaded = lorcana.loadFromResources()
        val parser = Parser()

        val matcher = parser.parse("(set:tfc & id:10) | (set:rotf & id:20)")

        loaded.cards.filter { matcher.match(it) }.forEach {
            println("card $it")
        }
    }
}
