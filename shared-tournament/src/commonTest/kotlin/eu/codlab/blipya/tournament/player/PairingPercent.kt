package eu.codlab.blipya.tournament.player

data class ColorCombination(
    val deck: Decks,
    val archetype: Archetype
)

data class PairingPercent(
    val combination1: ColorCombination,
    val combination2: ColorCombination,
    val deck1OverDeck2: Int
) {
    constructor(
        deck1: Decks,
        arch1: Archetype,
        deck2: Decks,
        arch2: Archetype,
        deck1OverDeck2: Int
    ) : this(ColorCombination(deck1, arch1), ColorCombination(deck2, arch2), deck1OverDeck2)

    private fun match(player: Player, combo: ColorCombination) =
        player.archetype == combo.archetype && player.deck == combo.deck

    fun matches(player1: Player, player2: Player): Boolean {
        if (match(player1, combination1) && match(player2, combination2)) return true
        if (match(player1, combination2) && match(player2, combination1)) return true

        return false
    }

    fun matches(pairing: PairingPercent): Boolean {
        if (combination1 == pairing.combination1 && combination2 == pairing.combination2) return true
        if (combination1 == pairing.combination2 && combination2 == pairing.combination1) return true

        return false
    }
}
