package eu.codlab.blipya.tournament.round

import eu.codlab.blipya.tournament.match.Group
import eu.codlab.blipya.tournament.player.Player

data class RoundGroup(
    val groups: List<Group>,
    val bye: Player? = null
)
