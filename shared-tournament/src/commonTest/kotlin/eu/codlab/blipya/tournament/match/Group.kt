package eu.codlab.blipya.tournament.match

import eu.codlab.blipya.tournament.player.Player
import korlibs.io.lang.assert
import korlibs.memory.isEven

data class Group(
    val players: List<Player>
) {
    init {
        assert(players.size.isEven)
    }

    val matches = players.shuffled()
        .chunked(2)
        .map { Match(it[0], it[1]) }

    fun results() = matches.map { it.result().toList() }.flatten()
}
