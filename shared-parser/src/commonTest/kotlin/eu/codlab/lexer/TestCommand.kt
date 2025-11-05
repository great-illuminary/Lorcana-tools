package eu.codlab.lexer

import eu.codlab.lorcana.Lorcana
import eu.codlab.lorcana.raw.SetDescription
import kotlin.test.Test
import kotlinx.coroutines.test.runTest

class TestCommand {
    @Test
    fun testLexer() = runTest {
        val lorcana = Lorcana()
        val loaded = lorcana.loadFromResources()
        val parser = Parser()

//        val matcher = parser.parse("(set:tfc & id:10) | (set:rotf & id:20)")
        val matcher = parser.parse("set:fab & !(variantset:tfc | variantset:rotf)")

        loaded.cards.filter{
            null != it.variants.find { variant ->
                variant.set == SetDescription.Fab && variant.id == 8
            }
        }.filter { matcher.match(it) }.forEach {
            println("card $it")
        }
    }
}
