package eu.codlab.blipya.tournament.player

import eu.codlab.lorcana.cards.InkColor
import kotlinx.serialization.Serializable
import eu.codlab.blipya.tournament.player.Archetype.Aggro
import eu.codlab.blipya.tournament.player.Archetype.Control
import eu.codlab.blipya.tournament.player.Archetype.Mid

@Serializable
data class DeckColor(
    val color1: InkColor,
    val color2: InkColor,
    val archetypes: List<Pair<Archetype, Int>>
) {
    constructor(
        color1: InkColor,
        color2: InkColor,
        vararg archetypes: Pair<Archetype, Int>
    ) : this(color1, color2, archetypes.toList())
}

@Serializable
enum class Archetype {
    Aggro,
    Mid,
    Control;

    companion object {
        fun list(vararg types: Archetype) = types.toList()
    }
}

enum class Decks(val deck: DeckColor) {
    SapSt(DeckColor(InkColor.Sapphire, InkColor.Steel, Aggro to 33, Mid to 33, Control to 33)),

    RubSap(DeckColor(InkColor.Ruby, InkColor.Sapphire, Control to 100)),

    AmbSte(DeckColor(InkColor.Amber, InkColor.Steel, Aggro to 50, Mid to 50)),

    AmeSte(DeckColor(InkColor.Amethyst, InkColor.Steel, Aggro to 50, Mid to 50)),

    AmbEm(DeckColor(InkColor.Amber, InkColor.Emerald, Aggro to 100)),

    EmSap(DeckColor(InkColor.Emerald, InkColor.Sapphire, Mid to 100)),

    RubSte(DeckColor(InkColor.Ruby, InkColor.Steel, Control to 50, Mid to 50)),

    EmSte(DeckColor(InkColor.Emerald, InkColor.Steel, Mid to 100)),

    AmeEm(DeckColor(InkColor.Emerald, InkColor.Steel, Aggro to 100)),

    AmeRub(DeckColor(InkColor.Amethyst, InkColor.Ruby, Aggro to 50, Control to 50)),
}