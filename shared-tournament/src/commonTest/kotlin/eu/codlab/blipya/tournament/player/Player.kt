package eu.codlab.blipya.tournament.player

data class Player(
    val deck: Decks,
    val archetype: Archetype,
    val points: Int = 0
) {
    fun addPointsCopy(add: Int) = copy(points = points + add)
}
