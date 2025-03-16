package eu.codlab.blipya.tournament

import eu.codlab.blipya.tournament.player.Decks
import eu.codlab.blipya.tournament.player.Player

class Tournament(
    val players: List<Player>
) {

}

fun createTournament(
    decks: List<Pair<Decks, Int>>
) = Tournament(
    players = decks.map { (color, number) ->
        (0..<number).map { _ ->
            // randomly take 1 archetype from the actual expected % of each archetypes
            val archetype = color.deck.archetypes.map { (arch, number) ->
                (0..<number).map { arch }
            }.flatten().random()

            Player(color, archetype)
        }
    }.flatten()
)
