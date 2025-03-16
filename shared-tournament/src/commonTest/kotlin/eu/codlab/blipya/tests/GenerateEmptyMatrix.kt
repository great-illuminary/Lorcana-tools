package eu.codlab.blipya.tests

import eu.codlab.blipya.tournament.player.ColorCombination
import eu.codlab.blipya.tournament.player.Decks
import eu.codlab.blipya.tournament.player.PairingPercent
import kotlinx.serialization.json.Json
import kotlin.test.Test

class GenerateEmptyMatrix {
    private val json = Json {
        encodeDefaults = true
        prettyPrint = true
        prettyPrintIndent = "  "
    }

    @Test
    fun printAnEmptyMatrix() {
        // we'd normally flatten everything down ->
        // val pairings = Decks.entries.map { deck1 ->
        //     Decks.entries.map { deck2 ->
        //         deck1.deck.archetypes.map { arch1 ->
        //             deck2.deck.archetypes.map { arch2 ->
        //                 PairingPercent(deck1, arch1, deck2, arch2, 0)
        //             }
        //         }
        //     }
        // }.flatten().flatten().flatten()

        var pairings = mutableListOf<PairingPercent>()
        Decks.entries.forEach { deck1 ->
            Decks.entries.forEach { deck2 ->
                deck1.deck.archetypes.forEach { arch1 ->
                    deck2.deck.archetypes.forEach { arch2 ->
                        val pairing = PairingPercent(
                            ColorCombination(deck1, arch1.first),
                            ColorCombination(deck2, arch2.first),
                            0
                        )

                        if (null == pairings.find { it.matches(pairing) }) {
                            pairings.add(pairing)
                        }
                    }
                }
            }
        }

        println(pairings.size)

        val results = "listOf(\n" + pairings.map { (c1, c2) ->
            "PairingPercent(${c1.deck.name}, ${c1.archetype.name}, ${c2.deck.name}, ${c2.archetype.name}, 0)"
        }.joinToString(",\n") + "\n)"

        // val results = json.encodeToString(
        //     ListSerializer(PairingPercent.serializer()),
        //     pairings
        // )

        println(results)
    }
}