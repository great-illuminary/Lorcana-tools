package eu.codlab.blipya.tournament.round

import eu.codlab.blipya.tournament.match.Group
import eu.codlab.blipya.tournament.player.Player
import korlibs.memory.isOdd


fun createGroups(players: List<Player>): RoundGroup {
    if (players.isEmpty()) return RoundGroup(emptyList())

    val max = players.maxOfOrNull { it.points } ?: 0

    val map: MutableMap<Int, MutableList<Player>> = mutableMapOf()
    //-1 to manage case where we have an odd of players with 0 points
    (-1..max).forEach { map[it] = mutableListOf() }

    val groups = (0..max).reversed()

    players.forEach { player -> map[player.points]?.let { it += player } }

    // range instead of keys since it's ordered
    groups.forEach { score ->
        val inGroup = map[score]!!

        if (inGroup.size.isOdd) {
            val removed = inGroup.removeLast()
            map[score - 1]!!.add(0, removed)
        }
    }

    return RoundGroup(
        groups.map { Group(map[it] ?: emptyList()) },
        map[-1]?.firstOrNull()
    )
}