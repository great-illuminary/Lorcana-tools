package eu.codlab.blipya.tournament.match

import eu.codlab.blipya.tournament.player.Pairings
import eu.codlab.blipya.tournament.player.Player

data class Match(
    val given1: Player,
    val given2: Player
) {
    fun result(): Pair<Player, Player> {
        val pairing = Pairings.find { it.matches(given1, given2) || it.matches(given2, given1) }
            ?: throw IllegalStateException("couldn't find pairing for ${given1.deck} & ${given2.deck}")

        val (player1, player2, reverse) = if (pairing.combination1.deck == given1.deck) {
            Triple(given1, given2, false)
        } else {
            Triple(given2, given1, true)
        }

        var player1RoundWon = 0

        (0..2).forEach {
            if (player1RoundWon == 2) return@forEach

            val won = (0..100).random() <= pairing.deck1OverDeck2
            if (won) player1RoundWon++
        }

        //TODO draw

        val result = when (player1RoundWon) {
            0 -> player1 to player2.addPointsCopy(3)
            1 -> player1 to player2.addPointsCopy(3)
            2 -> player1.addPointsCopy(3) to player2
            else -> throw IllegalStateException("couldn't manage invalid matches won : $player1RoundWon")
        }

        // println("Match finished = player1 score is now ${result.first.points} / player2 is ${result.second.points}")
        return if (reverse) {
            result.second to result.first
        } else {
            result
        }
    }
}