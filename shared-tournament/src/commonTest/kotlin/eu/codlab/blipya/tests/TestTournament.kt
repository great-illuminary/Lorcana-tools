package eu.codlab.blipya.tests

import eu.codlab.blipya.tournament.createTournament
import eu.codlab.blipya.tournament.player.Decks
import eu.codlab.blipya.tournament.round.Round
import korlibs.datastructure.iterators.parallelMap
import korlibs.memory.isEven
import kotlinx.coroutines.runBlocking
import kotlin.test.Test
import kotlin.test.assertEquals
import kotlin.test.assertTrue

class TestTournament {

    @Test
    fun `create a default tournament`() {
        val tournament = createTournament(expectedColors)

        assertEquals(expectedColors.sumOf { it.second }, tournament.players.size)
    }

    @Test
    fun `create the first round of the tournament`() = runBlocking {
        val possiblePlayers = mutableMapOf<Decks, Int>()
        Decks.entries.forEach { possiblePlayers[it] = 0 }

        (0..1000000).toList().map {
            println("doing $it")
            val tournament = createTournament(expectedColors)

            val expectedColorsButOddEven = expectedColors.mapIndexed { index, (color, number) ->
                if (index == 0) color to number + 1
                else (color to number)
            }

            listOf(expectedColors, expectedColorsButOddEven).map {
                var players = tournament.players
                (1..7).forEach {
                    val round = Round(players)

                    if (players.size.isEven) {
                        assertTrue(round.groups.bye == null)
                    } else {
                        assertTrue(round.groups.bye != null)
                    }

                    // println(round.groups)

                    val results = round.results()

                    // println(results)
                    assertEquals(tournament.players.size, results.size)

                    players = results
                }

                val finalPlayers = players.sortedByDescending { player -> player.points }
                val winner = finalPlayers.first()

                winner.deck
            }
        }.flatten().forEach { winner ->
            possiblePlayers[winner] = (possiblePlayers[winner] ?: 0) + 1
        }

        val total = possiblePlayers.values.sum()

        possiblePlayers.keys.forEach {
            println("$it = ${possiblePlayers[it]} / ${possiblePlayers[it]!! * 1.0 / total}")
        }
    }

    @Test
    fun `make a full tournament`() = runBlocking {
        val possiblePlayers = mutableMapOf<Decks, Int>()
        Decks.entries.forEach { possiblePlayers[it] = 0 }

        (0..100000).toList().map {
            val tournament = createTournament(expectedColors)

            var players = tournament.players
            (1..7).forEach {
                val round = Round(players)

                val results = round.results()

                assertEquals(tournament.players.size, results.size)

                players = results
            }

            val finalPlayers = players.sortedByDescending { player -> player.points }
            val winner = finalPlayers.first()

            winner.deck
        }.forEach { winner ->
            possiblePlayers[winner] = (possiblePlayers[winner] ?: 0) + 1
        }

        val total = possiblePlayers.values.sum()

        possiblePlayers.keys.forEach {
            println("$it = ${possiblePlayers[it]} / ${possiblePlayers[it]!! * 1.0 / total}")
        }
    }
}