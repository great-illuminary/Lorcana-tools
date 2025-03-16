package eu.codlab.blipya.tournament.round

import eu.codlab.blipya.tournament.player.Player

class Round(
    private val players: List<Player>
) {
    val groups = createGroups(players)

    fun results(): List<Player> {
        val players = groups.groups.map { it.results() }.flatten()

        return groups.bye?.let {
            players + it.copy(points = it.points + 7)
        } ?: players
    }
}
