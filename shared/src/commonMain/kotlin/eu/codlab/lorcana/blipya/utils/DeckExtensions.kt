package eu.codlab.lorcana.blipya.utils

import eu.codlab.lorcana.blipya.save.DeckModel
import eu.codlab.lorcana.blipya.save.ExpectedCardModel
import eu.codlab.lorcana.blipya.save.ScenarioModel
import eu.codlab.lorcana.math.Deck
import eu.codlab.lorcana.math.Scenario

fun DeckModel.toDeck(): Deck {
    return Deck(
        id = this.id,
        name = this.name,
        deckSize = this.size,
        defaultHand = this.hand,
    ).let { newDeck ->
        scenarii.forEach { scenar ->
            val scenario =
                /** */
                Scenario(
                    id = scenar.id,
                    name = scenar.name,
                    parent = newDeck,
                ) { _, _, _ ->
                    /** */
                }
            scenar.cards.forEach { holder ->
                scenario.add(
                    holder.id,
                    amount = holder.amount,
                    min = holder.min,
                    max = holder.max
                )
                scenario.update(holder.id, holder.name)
            }

            newDeck.addScenario(scenario)
        }

        newDeck
    }
}

fun Deck.toDeckModel(): DeckModel {
    return DeckModel(
        id = id,
        name = name,
        size = size,
        hand = hand,
        scenarii = scenarii.map { scenar ->
            println("setting the scenario's name in the json to ${scenar.name}")
            ScenarioModel(
                id = scenar.id,
                name = scenar.name,
                cards = scenar.cards.map { card ->
                    ExpectedCardModel(
                        id = card.id,
                        name = card.name,
                        amount = card.amount,
                        min = card.min,
                        max = card.max
                    )
                }
            )
        },
    )
}
